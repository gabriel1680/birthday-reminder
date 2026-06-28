@param org.gbl.SearchPageData data

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
@for(org.gbl.common.gateway.ContactResponse contact : data.pagination().values())
    <li data-contact-id="${contact.id()}">${contact.name()} | ${contact.birthdate()}</li>
@endfor
<style>
    body {
        font-family: Arial, sans-serif;
        max-width: 600px;
        margin: 40px auto;
    }

    select {
        margin: 5px 0;
    }

    h1, h2, h3 {
        margin-top: 1.5em;
        margin-bottom: 5px;
    }
</style>
</body>
</html>