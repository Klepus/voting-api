# curl samples:
 For windows use `Git Bash`

---
## Available for guests, users, admins:
### Get all restaurants (GET): 

    curl -X GET "http://localhost:8080/api/restaurants" -H "accept: application/json"

### Get all menus (GET):
Get all menus for today:

    curl -X GET "http://localhost:8080/api/restaurants/menus" -H "accept: application/json"

Get all menus for period: (Change period dates on actual)

    curl -X GET "http://localhost:8080/api/restaurants/menus?startDate=2021-04-27&end=2021-04-30" -H "accept: application/json"

### Get restaurant menu (GET)

Get restaurant menus for today:

    curl -X GET "http://localhost:8080/api/restaurants/1" -H "accept: application/json" 

Get restaurant menus for period: (Change period dates on actual)

    curl -X GET "http://localhost:8080/api/restaurants/1?startDate=2021-04-27&end=2021-04-30" -H "accept: application/json"

### Get all Votes (GET):
Get all votes for today:

    curl -X GET "http://localhost:8080/api/votes" -H "accept: application/json"

Get all votes for period: (Change period dates on actual)

    curl -X GET "http://localhost:8080/api/votes?startDate=2021-04-27&end=2021-04-30" -H "accept: application/json"

-------
## Available for users:
### Vote for restaurant (POST):

    curl -X POST "http://localhost:8080/api/profile/restaurants/1" -H "accept: application/json" -H "accept: application/json" --user user@yandex.ru:password

------
## Available for Admins:
### Create restaurant (POST):

    curl -X POST "http://localhost:8080/api/admin/restaurants" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"restaurant_name\": \"New Restaurant\", \"restaurant_address\": \"Street 1, 1\", \"restaurant_telephone\": \"000 000 000\"}" --user admin@gmail.com:admin

### Create menu (POST):

    curl -X POST "http://localhost:8080/api/admin/restaurants/3/menu" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"restaurant_menu\": [ { \"dish_name\": \"Spaghetti\", \"dish_price\": 17.99 }, { \"dish_name\": \"Coffee cappuccino or cup of tea\", \"dish_price\": 2.99 } ]}" --user admin@gmail.com:admin

### Update restaurant (PUT):

    curl -X PUT "http://localhost:8080/api/admin/restaurants/1" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"restaurant_name\": \"New restaurant name\", \"restaurant_address\": \"New address\", \"restaurant_telephone\": \"000 000 000\"}" --user admin@gmail.com:admin

### update menu (PUT):

    curl -X PUT "http://localhost:8080/api/admin/restaurants/2/menu" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"restaurant_menu\": [ { \"dish_name\": \"beef stroganoff\", \"dish_price\": 18.99 }, { \"dish_name\": \"baklava and tea\", \"dish_price\": 9.99 } ]}" --user admin@gmail.com:admin

### Delete restaurant (DELETE):

    curl -X DELETE "http://localhost:8080/api/admin/restaurants/1" -H "accept: application/json" --user admin@gmail.com:admin

### Delete menu (DELETE):

    curl -X DELETE "http://localhost:8080/api/admin/restaurants/2/menu" -H "accept: application/json" --user admin@gmail.com:admin

