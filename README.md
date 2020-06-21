# kkop-coupon

### 개발환경
* Java 8
* SpringBoot 2.2.1
* Gradle 
* JPA
* H2
#


### H2를 설치/구동이 필요합니다.
* https://www.h2database.com
* 다운로드 및 설치
* ../h2/bin/h2.sh 실행
* 데이터베이스 파일 생성 방법
  * jdbc:h2:~/kkopservice (최소 한번)
  * ~/coupon.mv.db 파일 생성 확인
  * 이후 부터는 jdbc:h2:tcp://localhost/~/kkopservice 로 접속하면 됩니다. 
#

### APIs (Swagger 문서를 참고하세요.)
http://localhost:8080/swagger-ui.html#/

#


###### Postman 파일을 첨부하였습니다. 
kkopservice.postman_collection.json


