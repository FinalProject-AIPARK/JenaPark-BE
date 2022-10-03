from scipy.io.wavfile import write
import numpy as np
import uuid


# 오디오 파일 생성 / data -> 텍스트와 성우(성우는 사용하지 않습니다.)
def audio_request(data, path='result', sr=44100):
    audio_id = uuid.uuid4().hex[:8]
    output = path + '/' + audio_id + '.wav'
    text = data['text']
    # 오디오 생성
    length = np.random.randint(1, len(text) // 2 + 2)
    audio = np.random.rand(sr*length)
    write(output, sr, audio.astype(np.int16))

    return audio_id
