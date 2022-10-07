import sys
from s3 import s3_connection, s3_get_object
from media import video_request
import os


access_key = sys.argv[1]
secret_key = sys.argv[2]
region = sys.argv[3]
audioFileS3Path = sys.argv[4]
avatarFileS3Path = 'avatar/pho/pho-1-1-1.png'
s3 = s3_connection(region, access_key, secret_key)

def createVideo():
#     date = dict()
    s3_get_object(s3, 'jenapark', 'result/', audioFileS3Path)
    audioId = audioFileS3Path.split('/')[-1]
    s3_get_object(s3, 'jenapark', 'result/', avatarFileS3Path)
    avatarId = avatarFileS3Path.split('/')[-1]
    video_id = video_request(audioId, avatarId, 'ㅎㅎ', 'result/')
    return True


createVideo()
