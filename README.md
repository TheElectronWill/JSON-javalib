# JSON-javalib
This is a fast and simple [JSON](json.org) library for Java.

## How to use
The important class is Toml.java. It contains public static methods for reading and writing JSON data.
You can read data like this:

```java
//import com.electronwill.json.Json
File file = new File("myFile.json");
FileReader reader = new FileReader(file);
Map<String, Object> jsonObject = Json.readObject(reader);
```

And write data like this:
```java
//import com.electronwill.json.Json
File file = new File("myFile.json");
FileWriter writer = new FileWriter(file);
Map<String, Object> jsonObject = ...//put your data here
boolean humanFriendly = true;//true to indent and space the output, false to compact it
Json.write(jsonObject, writer, humanFriendly);
```

You can also use `JsonReader` and `JsonWriter` directly.

## Data types
The JSON data is mapped to the following java types:

   JSON | Java
------- | -------
Integer | `int` or `long` (it depends on the size)
Decimal | `double`
Boolean | `boolean`
String  | `String`
Array   | `List<Object>`
Object  | `Map<String, Object>`
null    | `null`

## Java version
This library requires Java 7.
