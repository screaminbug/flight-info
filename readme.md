# Flights Info
Api which stores, updates and retrieves simple flight information data
[http://tomislav-strelar.from.hr](http://tomislav-strelar.from.hr)


## Version: 1.0.0

### Terms of service
N/A

**Contact information:**  
tstrelar@gmail.com  

**License:** [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

### Security
**flightinfo_auth**  

|oauth2|*OAuth 2.0*|
|---|---|
|Token URL|http://localhost:8180/auth/realms/flight-info/protocol/openid-connect/token/introspect|
|Flow|application|
|**Scopes**||
|write:flights|modify flights in your account|
|read:flights|read flights|

### /flight

#### POST
##### Summary:

Add new flight to flight info store

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| body | body | A flight information that needs to be added to the repo | Yes | [Flight](#flight) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | An ApiResponse object | [ApiResponse](#apiresponse) |
| 202 | Accepted |  |
| 400 | Invalid input |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| flightinfo_auth | write:flights | read:flights |

#### PUT
##### Summary:

Update an existing flight

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| body | body | Flight object that needs to be updated | Yes | [Flight](#flight) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | An ApiResponse object | [ApiResponse](#apiresponse) |
| 202 | Accepted |  |
| 400 | Invalid input |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| flightinfo_auth | write:flights | read:flights |

### /flight/find

#### GET
##### Summary:

Finds flights by departure and arrival locations, departure and arrival dates, number of transfers in outbound and inbound directions, number of passengers, currency, price and operating company

##### Description:

Multiple status values can be provided with comma separated strings

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| departure-iata | query | Departure location that need to be considered for filter (IATA codes) | No | [ string ] |
| arrival-iata | query | Arrival location that need to be considered for filter (IATA codes) | No | [ string ] |
| date-dpt-from | query | The start date and time of departure | Yes | dateTime |
| date-dpt-to | query | The end date and time of departure | Yes | dateTime |
| date-arr-from | query | The start date and time of arrival | Yes | dateTime |
| date-arr-to | query | The end date and time of arrival | Yes | dateTime |
| transfer-count | query | The number of transfers | No | [ integer ] |
| passenger-count | query | The number of passengers | No | [ integer ] |
| company | query | The company that is providing the flight | No | [ string ] |
| flight-id | query | The company flight identifier | No | [ string ] |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | successful operation | [ [Flight](#flight) ] |
| 400 | Invalid status value |  |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| flightinfo_auth | write:flights | read:flights |

### /resource/{messageId}

#### GET
##### Summary:

Retrieve resource

##### Description:

Retrieve resource returned by 202 'accepted' response. This can be a result of asynchronous search, update or create operation

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| messageId | path | ID of resorce retrieved by previous request | Yes | long |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | successful operation | [ApiResponse](#apiresponse) |
| 400 | Invalid ID supplied |  |
| 404 | Pet not found |  |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| flightinfo_auth | write:flights | read:flights |

### Models


#### Flight

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| company | string | The operating company name | No |
| id | string (uuid) | The flight unique identifier | No |
| airportDeparture | string | IATA code of the departure airport | No |
| airportArrival | string | IATA code of the arrival airport | No |
| dateDeparture | dateTime | Date and time of departure | No |
| dateArrival | dateTime | Date and time of arrival | No |
| numberOfTransfers | integer | The number of transfers | No |
| numberOfPassengers | integer | The number of passengers for the flight | No |
| flightId | string | A company flight identifier | No |

#### ApiResponse

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| messageId | string (uuid) | Both flight id as well as resource reference to be used to get deffered response | No |
| status | [ApiResponse_status](#apiresponse_status) |  | No |
| flight | [Flight](#flight) |  | No |
| flights | [ [Flight](#flight) ] |  | No |

#### ApiResponse_status

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| statusCode | string | The status code of the processing result | No |
| statusMessage | string | More info about the status | No |


##Messaging
Frontend and Backend communicate via ActiveMQ Artemis.
Adjust following properties in ```application.properties``` of Frontend and Backend modules
as needed:
- ```spring.artemis.host```
- ```spring.artemis.port```
- ```spring.artemis.user```
- ```spring.artemis.password```

##Authentication and Authorization

###Keycloak
Frontend uses OAuth2 for authentication and authorization. The application is setup to use Keycloak 
specifically. You can get it [here](https://www.keycloak.org/downloads.html). To use it a keycloak user
must be created by running ```add-user-keycloak``` script in bin directory.

As of version 7.0, keycloak is not allowing import of scripts, but that can be enabled by 
setting a ```keycloak.profile.feature.upload_scripts``` property to ```enabled```. So set it 
by passing it to the standalone batch/shell script found in bin directory.

Now the realm data can be imported, but the realm must be created first. Create new realm ```flight-info``` and import
```realm-export.json``` file (from ```external``` directory in the repo) with overwriting.

Unfortunately, this import is only partial. Users are not imported. So they must be created. 
Create at least two users, and ```ADMIN``` and ```USER``` roles of ```flight-info``` client 
to them. 

Next, a secret must be regenerated in ```flight-info``` client for use when requesting token.

###Application properties

The following application properties needs to be set for authorization with Keycloak

- ```rest.security.issuer-uri``` - realm URI
- ```security.oauth2.resource.id``` - the name of the realm 
- ```security.oauth2.resource.token-info-uri``` - introspection URI
- ```security.oauth2.resource.user-info-uri``` - user info URI
- ```security.oauth2.resource.jwt.key-value``` - the public key for verifying JWT tokens

Defaults are set in ```application.properies``` of the backend module. The only thing that
will need to be changed is the public key. That can be retrieved in ```Realm Settings --> Keys --> Public Key```

