from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import numpy as np
import pandas as pd
from tensorflow.keras.models import load_model

# 1️⃣ Créer l'app AVANT tout décorateur
app = FastAPI()

# 2️⃣ Charger modèles
ALPHA = 0.85

print("Chargement des modèles...")

model_xgb = joblib.load('model_xgb_hybrid.pkl')
model_lstm = load_model('model_lstm_hybrid.keras')
scaler_x = joblib.load('scaler_x.pkl')
scaler_y = joblib.load('scaler_y.pkl')

print("✅ Système prêt")

# 3️⃣ Définir le modèle de données
class PredictionRequest(BaseModel):
    air_temperature: float
    dew_temperature: float
    hour: int
    dayofweek: int
    month: int
    is_weekend: int
    cons_h_1: float
    cons_h_24: float
    conso_moy_6h: float
    conso_moy_24h: float

# 4️⃣ Définir l'endpoint APRÈS avoir créé app
@app.post("/predict")
def predict(data: PredictionRequest):

    print("🔥 FASTAPI RECEIVED REQUEST")

    try:
        features = [
            'air_temperature', 'dew_temperature', 'hour', 'dayofweek',
            'month', 'is_weekend', 'cons_h_1', 'cons_h_24',
            'conso_moy_6h', 'conso_moy_24h'
        ]

        values = [getattr(data, f) for f in features]
        df_input = pd.DataFrame([values], columns=features)

        scaled_input = scaler_x.transform(df_input)

        pred_xgb = model_xgb.predict(df_input)[0]

        lstm_input = scaled_input.reshape((1, 1, len(features)))
        pred_lstm_scaled = model_lstm.predict(lstm_input, verbose=0)
        pred_lstm = scaler_y.inverse_transform(pred_lstm_scaled).flatten()[0]

        final_val = (ALPHA * pred_xgb) + ((1 - ALPHA) * pred_lstm)

        return {
            "status": "success",
            "prediction_kwh": round(float(final_val), 2)
        }

    except Exception as e:
        print("❌ ERROR:", str(e))
        return {"status": "error", "message": str(e)}