from urllib import response
from flask import Flask, request
import uuid
import time
import os
import boto3
import sys

from scipy.io import wavfile
from scipy.io.wavfile import write
import numpy as np
from PIL import Image, ImageDraw, ImageFont

app = Flask(__name__)

access_key = sys.argv[1]
secret_key = sys.argv[2]
region = sys.argv[3]

print(access_key)
print(secret_key)
print(region)

# s3 커넥션 설정
def s3_connection():
    try:
        s3 = boto3.client(
            service_name="s3",
            region_name=region,  # 자신이 설정한 bucket region
            aws_access_key_id=access_key,
            aws_secret_access_key=secret_key,
        )
    except Exception as e:
        print(e)
    else:
        print("## s3 bucket connected! ##")
        return s3


s3 = s3_connection()

# upload 하기
def s3_put_object(s3, bucket, filepath, access_key):
    """
    s3 bucket에 지정 파일 업로드
    :param s3: 연결된 s3 객체(boto3 client)
    :param bucket: 버킷명
    :param filepath: 파일 위치
    :param access_key: 저장 파일명
    :return: 성공 시 True, 실패 시 False 반환
    """
    try:
        s3.upload_file(
            Filename=filepath,
            Bucket=bucket,
            Key=access_key,
        )
    except Exception as e:
        print(e)
        print("실패!!!")
        return False
    print("upload 성공")
    return True


@app.route('/', methods=['GET'])
def index():
    return {'status': 'hello'}

# 오디오 요청
@app.route('/request_audio', methods=['GET','POST'])
def audio():
        response_object = {'status': 'success'}
        # 예시
        if request.method == "GET":
            data = dict()
            data['text'] = '여기에는  텍스트가 들어갑니다. 텍스트가 길수록 오디오는 길어집니다.'
            data['narration'] = 'none'
            path = 'result'
            audio_id = audio_request(data, path)
            print(f"{audio_id}.wav가 생성되었습니다.")
            s3_put_object(s3, "jenapark", f"result/{audio_id}.wav",
                          f"audio/{audio_id}.wav")
            return response_object

        else:
            post_data = request.get_json()
            data = dict()
            try:
                data['text'] = post_data['text']
                data['narration'] = post_data['narration']
                path = post_data['path']
                audio_id = audio_request(data, path)
                response_object['id'] = audio_id
            except:
                response_object['status'] = 'fail'
            return response_object


# 오디오 파일 생성 / data -> 텍스트와 성우(성우는 사용하지 않습니다.)
def audio_request(data, path='result', sr=44100):
    audio_id = uuid.uuid4().hex[:8]
    output = path + '/' + audio_id + '.wav'
    text = data['text']
    narration = data['narration']
    # 오디오 생성
    length = np.random.randint(len(text)//5, len(text)//4)
    audio = np.random.rand(sr*length)
    write(output, sr, audio.astype(np.int16))

    return audio_id

# 포트를 변경하셔도 됩니다.
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8001)