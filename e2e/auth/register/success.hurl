POST {{BASE_URL}}/auth/register
Content-Type: application/json
{
  "username": "{{TEST_USERNAME}}",
  "password": "{{TEST_PASSWORD}}",
  "email": "{{TEST_EMAIL}}"
}
HTTP/1.1 200
[Asserts]
jsonpath "$.username" exists
jsonpath "$.email" exists
jsonpath "$.username" == "{{TEST_USERNAME}}"
jsonpath "$.email" == "{{TEST_EMAIL}}"
