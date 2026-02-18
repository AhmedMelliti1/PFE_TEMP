from flask import Flask, request, jsonify
import joblib
import pandas as pd
from datetime import datetime

app=Flask(__name__)

print("Chargement du modèle...")
try:
    model=joblib.load('random_forest_model.pkl')
    print("Modèle chargé avec succès")
except Exception as e:
    print(f"Erreur lors du chargement du modèle: {e}")
    model=None

@app.route('/predict', methods=['POST'])
def predict():
    if not model:
        return jsonify({'error': 'Modèle non chargé'}), 500
    try:
        data = request.get_json()
        temp=data.get('temperature')
        data_str=data.get('timestamp')
        dt=datetime.fromisoformat(data_str)
        input_data= pd.DataFrame({
            'air_temperature': [temp],
            'hour': [dt.hour],
            'dayofweek': [dt.weekday()],
            'month': [dt.month],
            'is_weekend': [1 if dt.weekday() >= 5 else 0]
        })

        prediction=model.predict(input_data)[0]

        return jsonify({
            'status': 'success',
            'timestamp': data_str,
            'prediction': prediction
        })
        
    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}),400

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)