POST {{BASE_URL}}/auth/register
Content-Type: application/json
{
}
HTTP/1.1 400
[Asserts]
jsonpath "$.username" exists
jsonpath "$.email" exists
jsonpath "$.password" exists
jsonpath "$.username" == "Kitöltése kötelező"
jsonpath "$.email" == "Kitöltése kötelező"
jsonpath "$.password" == "Kitöltése kötelező"
