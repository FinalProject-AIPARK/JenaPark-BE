from scipy.io import wavfile
from scipy.io.wavfile import write
import time
import os
from PIL import Image, ImageDraw, ImageFont
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


def video_request(audioId, avatarId, path):
    wave = path + audioId
    sr, wav = wavfile.read(wave)
    length = float(len(wav)/sr)
    temp_image = path + avatarId
    # h, w = 1920, 1080
    # color_background = (30, 50, 90)
    # color_text_avatar = (220, 50, 90)
    # color_text_background = (230, 220, 120)

    # xy_text_avatar = (100, 150)
    # xy_text_background = (100, 100)

    # font_avatar = ImageFont.truetype('font/BMHANNAAir_ttf.ttf', 100)
    # font_background = ImageFont.truetype('font/BMHANNAAir_ttf.ttf', 50)

    # background_image = np.zeros((h, w, 3), np.uint8)
    # background_image[:] = color_background
    # background_image = Image.fromarray(background_image)

    # draw = ImageDraw.Draw(background_image)
    # draw.text(xy_text_avatar, avatar,
    #           fill=color_text_avatar, font=font_avatar)
    # draw.text(xy_text_background, background,
    #           fill=color_text_background, font=font_background)

    # background_image.save(temp_image)
    video_id = uuid.uuid4().hex[:16]
    output = path + video_id + '.mp4'
    time.sleep(length)
    cmd = 'ffmpeg -y -loop 1 -i {} -c:v libx264 -t {} -pix_fmt yuv420p {}'.format(
        temp_image, length, output)
    os.system(cmd)
    os.remove(wave)
    os.remove(temp_image)
    return video_id
