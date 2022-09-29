import boto3

# s3 커넥션 설정
def s3_connection(region, access_key, secret_key):
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