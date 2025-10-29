POST {{BASE_URL}}/auth/register
Content-Type: application/json
{
  "username": "duplicated_username",
  "password": "StrongPass1",
  "email": "email1@emai.com"
}
HTTP/1.1 200
[Asserts]
jsonpath "$.username" exists
jsonpath "$.email" exists

POST {{BASE_URL}}/auth/register
Content-Type: application/json
{
    "username": "duplicated_username",
    "password": "StrongPass1",
    "email": "email2@emai.com"
}
HTTP/1.1 400
[Asserts]
jsonpath "$.message" exists
jsonpath "$.message" == "Ez a felhasználónév már foglalt!"
