import sys
from s3 import s3_put_object, s3_connection
from media import audio_request
import os


access_key = sys.argv[1]
secret_key = sys.argv[2]
region = sys.argv[3]
allText = sys.argv[4]
s3 = s3_connection(region, access_key, secret_key)


def createAudios(allText):
    texts = [text.strip()+'.' for text in list(filter(lambda x: x !=
                                                      '', allText.split('.')))]

    for text in texts:
        print(text)
        data = dict()
        data['text'] = text
        path = 'result'
        audio_id = audio_request(data, path)
        s3_put_object(s3, "jenapark", f"result/{audio_id}.wav",
                      f"audio/{audio_id}.wav")
        os.remove(f"result/{audio_id}.wav")
    return True


createAudios(allText)
