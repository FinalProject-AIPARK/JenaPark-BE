from scipy.io import wavfile
from scipy.io.wavfile import write
import time
import os
import numpy as np
from moviepy.editor import *
from gtts import gTTS
import uuid


# 오디오 파일 생성 / data -> 텍스트와 성우(성우는 사용하지 않습니다.)
def audio_request(data, path='result', sr=44100):
    audio_id = uuid.uuid4().hex[:8]
    output = path + '/' + audio_id + '.wav'
    text = data['text']
    # 오디오 생성
    tts = gTTS(text=text, lang='ko')
    tts.save(output)
    return audio_id


def video_request(audioId, avatarId, path):
    # result/{audioId}
    wave = path + audioId

    # result/{avatarId}
    avatar_image = path + avatarId

    video_id = uuid.uuid4().hex[:32]
    output = path + video_id + '.mp4'

    audio_path = AudioFileClip(wave)
    video_path = ImageClip(avatar_image, duration=audio_path.duration)
    video_path = video_path.set_audio(audio_path)
    video_path.write_videofile(output, fps=24, codec='libx264')

    os.remove(wave)
    os.remove(avatar_image)
    return video_id
