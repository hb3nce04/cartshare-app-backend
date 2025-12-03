POST {{BASE_URL}}/auth/login
Content-Type: application/json
{
  "username": "{{TEST_USERNAME}}",
  "password": "{{TEST_PASSWORD}}"
}
HTTP/1.1 200
[Asserts]
jsonpath "$.refreshToken" exists
jsonpath "$.accessToken" exists
