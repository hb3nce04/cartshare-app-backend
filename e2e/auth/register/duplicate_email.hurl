POST {{BASE_URL}}/auth/register
Content-Type: application/json
{
  "username": "user1",
  "password": "StrongPass1",
  "email": "duplicated_email@email.com"
}
HTTP/1.1 200
[Asserts]
jsonpath "$.username" exists
jsonpath "$.email" exists

POST {{BASE_URL}}/auth/register
Content-Type: application/json
{
    "username": "user2",
    "password": "StrongPass1",
    "email": "duplicated_email@email.com"
}
HTTP/1.1 400
[Asserts]
jsonpath "$.message" exists
jsonpath "$.message" == "Ez az e-mail cím már foglalt!"
