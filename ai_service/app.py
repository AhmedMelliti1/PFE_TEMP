from flask import Flask, request, jsonify
import joblib
import numpy as np
import pandas as pd
from tensorflow.keras.models import load_model

app = Flask(__name__)

# --- CONFIGURATION ---
ALPHA = 0.85  # Le poids donné à XGBoost (comme validé lors de vos tests)

print("Chargement des modèles et des scalers...")
try:
    # Chargement de l'intelligence XGBoost
    model_xgb = joblib.load('model_xgb_hybrid.pkl')
    
    # Chargement de l'intelligence LSTM (Format .keras recommandé)
    model_lstm = load_model('model_lstm_hybrid.keras')
    
    # Chargement des outils de mise à l'échelle (Scalers)
    scaler_x = joblib.load('scaler_x.pkl')
    scaler_y = joblib.load('scaler_y.pkl')
    
    print("✅ Système Hybride prêt à prédire !")
except Exception as e:
    print(f"❌ Erreur de chargement : {str(e)}")

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # 1. Récupération des données envoyées par Spring Boot
        data = request.get_json()
        
        # Liste ordonnée des caractéristiques (doit être identique à l'entraînement)
        features = [
            'air_temperature', 'dew_temperature', 'hour', 'dayofweek', 
            'month', 'is_weekend', 'cons_h_1', 'cons_h_24', 
            'conso_moy_6h', 'conso_moy_24h'
        ]
        
        # Extraction des valeurs
        values = [float(data[f]) for f in features]
        
        # 2. Préparation pour les modèles
        # Conversion en DataFrame (nécessaire pour garder le nom des colonnes du scaler)
        df_input = pd.DataFrame([values], columns=features)
        
        # Mise à l'échelle (Scaling)
        scaled_input = scaler_x.transform(df_input)
        
        # 3. Prédiction XGBoost (sur données scalées ou brutes selon votre entraînement final)
        # Note: Si vous avez entraîné XGB sur X_train_fix (brut), utilisez 'df_input' ici
        pred_xgb_raw = model_xgb.predict(df_input) # On utilise les valeurs réelles pour XGB
        
        # 4. Prédiction LSTM (Format 3D obligatoire)
        lstm_input = scaled_input.reshape((1, 1, len(features)))
        pred_lstm_scaled = model_lstm.predict(lstm_input, verbose=0)
        # Inversion du scale pour avoir des kWh
        pred_lstm = scaler_y.inverse_transform(pred_lstm_scaled).flatten()[0]
        
        # 5. FUSION HYBRIDE 🚀
        final_val = (ALPHA * pred_xgb_raw[0]) + ((1 - ALPHA) * pred_lstm)
        
        return jsonify({
            'status': 'success',
            'prediction_kwh': float(round(final_val, 2)),
            'details': {
                'xgb_contribution': float(round(pred_xgb_raw[0], 2)),
                'lstm_contribution': float(round(pred_lstm, 2)),
                'method': 'Hybrid Blending (XGB + LSTM)'
            }
        })

    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 400

if __name__ == '__main__':
    # On écoute sur le port 5000
    app.run(host='0.0.0.0', port=5000, debug=False)