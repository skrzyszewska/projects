<?php
$servername = "212.182.24.105:3306";
$username = "test";
$password = "Apteka2018";
$db_name = "test";

// Create connection
$mysqli = new mysqli($servername, $username, $password, $db_name);

$myArray = array();

$query = $_REQUEST['query'];

if ($result = $mysqli->query("$query")) {

   while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
   }
    echo json_encode($myArray);
}


$result->close();
$mysqli->close();

?>