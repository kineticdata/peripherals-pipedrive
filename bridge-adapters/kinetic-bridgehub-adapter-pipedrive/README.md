# Kinetic Bridgehub Adapter Pipedrive

This Rest based bridge adapter was designed to work with Pipedrive services v1 rest api.
___
## Adapter Configurations
Name | Description
------------ | -------------
Token | The application token
___
## Supported structures
Name | Parameter * required
------------ | -------------
Deals | id
Persons | id
Adhoc | 
___
## Example Qualification Mapping
* user_id=55555555
___
## Notes
* [JsonPath](https://github.com/json-path/JsonPath#path-examples) can be used to access nested values. The root of the path is values.
* To get a single person or deal provide id=${id}
