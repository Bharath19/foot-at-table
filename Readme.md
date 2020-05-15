![CI](https://github.com/Country-Broot/country-broot-backend/workflows/CI/badge.svg)


Swagger Ui Path : http://localhost:8080/foodattable/api/swagger-ui.html#/

Swagger Doc Path : http://localhost:8080//foodattable/api/v2/api-docs

RestaurantApi Input Json :

{
	"restaurantName": "test777",
	"restaurantPhoneNumber": "9078564738",
	"restaurantEmailId": "test2@gmail.com",
	"fssaiLicenseNo": "7856347889",
	"website": "test.com",
	"submittedBy": "Manager",
	"phoneNumber": "9876547698",
	"emailId": "person@gmail.com",
	"restaurantIsOpened": false,
	"restaurantOpenDate": "2020-04-30",
	"accountId": 1,
	"status": "open",
	"state": "draft",
	"tierId": 1,
	"avgRatePerPerson": 4.3,
	"imageUrl": "http://test.com",
	"description": "desc",
	"alcoholServed": true,
	"address": {
		"line1": "Ammapet",
		"line2": "",
		"district": "salem",
		"city": "salem",
		"state": "tamilnadu",
		"country": "india",
		"pincode": "636003",
		"lattitude": "",
		"longitude": ""
	},
	"type": [1,2],
	"services": [1,2],
	"seating": [1,2],
	"payment": [2,3],
	"cuisines": [1],
	"searchTags": [1],
	"dietId": [3],
	"timings": [
			        {
						"day": "Monday",
						"from": "06:00 PM",
						"to": "11:50 PM"
					},
					{
						"day": "Tuesday",
						"from": "04:00 PM",
						"to": "11:00 PM"
					}
			   ]
}

Query need to run before calling restaurantapi :

1) Account_Detail Table

		INSERT INTO `skipthequeue`.`account_detail` (`created_at`, `name`, `updated_at`) VALUES (now(), 'testaccount', now());

2) Tiers Table

		INSERT INTO `skipthequeue`.`tiers` (`created_at`, `name`, `updated_at`) VALUES (now(), 'testtier', now());

3) Search_Type Table

		INSERT INTO `skipthequeue`.`search_type` (`created_at`, `name`, `updated_at`) VALUES (now(), 'searchtype', now());

4) Cuisines Table

		INSERT INTO `skipthequeue`.`cuisines` (`created_at`, `name`, `updated_at`) VALUES (now(), 'cuisines', now());

5) Diets Table

		INSERT INTO `skipthequeue`.`diets` (`created_at`, `name`, `updated_at`) VALUES (now(), 'veg', now());
		INSERT INTO `skipthequeue`.`diets` (`created_at`, `name`, `updated_at`) VALUES (now(), 'non-veg', now());
		INSERT INTO `skipthequeue`.`diets` (`created_at`, `name`, `updated_at`) VALUES (now(), 'veg,non-veg', now());
		
Food Api Json
{       "name": "Veg Pizza",
		"description": "Pizza",
		"tags":[1,2],
		"restaurantId":1,
		"imageUrl": "www.images.com",
		"dietId": 1,
		"cuisineId":1,
		"startTime":"9:00",
		"endTime":"12:00",
		"sortNo":1,
		"price":400,
		"status":"active",
		"foodCategoryId":1,
		"extras":[
			{                   "name": "Cheese Topping",
    							"type": "multiple",
    							"description": "Cheese",
    							"sortNo": 1,
    							"status": "active",
    							"foodOptionsModels": [
            						{
            						"name": "Cheese Burst",
            						"description": "Cheese Burst",
            						"price": 25,
           							"imageUrl": "url",
        							"status": 1,
        							"sortNo": 1
            						},
            						{
            						"name": "Double Cheese",
            						"description": "Double Cheese",
            						"price": 25,
           							"imageUrl": "url",
        							"status": 1,
        							"sortNo": 2
            						}
                                ]			
			
		           }
			     ]
}


//make new order
{
  "carts": [
    {
      "foodId": 4,
      "quantity": 2,
      "state": "requested"
    },
    {
      "foodId": 4,
      "quantity": 10,
      "state": "requested"
    }
  ],
  "orderType": 1,
  "restaurantId": 1,
  "restaurantTableId": 1,
  "state": "requested",
  "userId": 1
}



