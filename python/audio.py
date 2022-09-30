import sys
from s3 import s3_put_object, s3_connection
from scipy.io.wavfile import write
import numpy as np
import uuid
import os

print("check")
access_key = sys.argv[1]
secret_key = sys.argv[2]
region = sys.argv[3]

s3 = s3_connection(region, access_key, secret_key)


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


def audio():
    data = dict()
    data['text'] = '여기에는  텍스트가 들어갑니다.'
    data['narration'] = 'none'
    path = 'result'
    audio_id = audio_request(data, path)
    s3_put_object(s3, "jenapark", f"result/{audio_id}.wav",
                  f"audio/{audio_id}.wav")

    os.remove(f"result/{audio_id}.wav")


audio()
