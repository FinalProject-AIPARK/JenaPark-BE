import sys
from s3 import s3_put_object, s3_connection
from media import video_request
import os


access_key = sys.argv[1]
secret_key = sys.argv[2]
region = sys.argv[3]
job = sys.argv[4]
s3 = s3_connection(region, access_key, secret_key)

def createVideo (job):
    date = dict()

#     video_id = video_request(id, avatar, background, path)
    return True

createVideo(job)