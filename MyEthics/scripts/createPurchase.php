<?php
require_once("config.php");

$error = "";
$success = 1;

function main(){
	global $error;
	global $success;
	global $conn;
	$user_id;
	$date;
	if($_SERVER["REQUEST_METHOD"] == "POST"){
		$product_name;
		$product_id;
		if(!empty($_POST["user_id"])){
			$user_id = intval(trim($_POST["user_id"]));
		}else{
			$error = "no user id";
			$success = 0;
		}
		if(!empty($_POST["name"])){
			$product_name = strtolower(trim($_POST["name"]));
		}else{
			$error = "No product name.";
			$success = 0;
		}
		if(!empty($_POST["date"])){
			$date = trim($_POST["date"]);
		}else{
			$error = "No date";
			$success = 0;
		}
		if(!empty($_POST["tags"])){
			$tags = json_decode($_POST["tags"]);
			//$error = "no tags";
			//$success = 0;
		}
		if($success == 1){
			//check if product exists and insert into products if it doesn't
			$product_id = getProductId($product_name);
			if($product_id == 0){
				//insert_product
				createProduct($product_name);
				$product_id = getProductId($product_name);
			}				
			//insert product tags
			foreach($tags as $t_tag){
				if(!isTagCreated($t_tag)){
					createTag($t_tag);
				}
			}	
		}
		
		//insert purchase
		if($success == 1){
			createPurchase($product_id, $date, $user_id);
		}
		
		//purchases_has_tags
		foreach($tags as $t_tag){
			$t_tag_id = getTagId($t_tag);
			createProductHasTags($t_tag_id, $product_id, $t_tag);
		}	
			
	}else{
		$error = "No post method.";
		$success = 0;	
	}
	error_log($conn->error);
	$conn->close();
	$data = ["success" => $success, "error" => $error];
	header("Content-Type:application/json");
	echo json_encode($data);
}
//takes in product name return its id
function getProductId($product_name){
	global $error;
	global $success;
	global $conn;
	$sql = "SELECT idproducts FROM products WHERE product_name LIKE ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_product_name);
		$param_product_name = $product_name;
		$stmt->bind_result($t_product_id);
		if($stmt->execute()){
			$stmt->store_result();
			if($stmt->num_rows() > 0){
				$stmt->fetch();
				$product_id = $t_product_id;
				$stmt->close();
				return $product_id;
			}
		}else{
			$error = "no results";
			$success = 0;
		}
		$stmt->close();
	}else{
		$error = "bad query";
		$success = 0;
	}
	return 0;
}

function createProduct($product_name){
	global $error;
	global $success;
	global $conn;
	$sql = "INSERT INTO products(product_name) VALUES (?)";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_product_name);
		$param_product_name = $product_name;
		if($stmt->execute()){
			return true;
		}else{
			$error = "bad query";
			$success = 0;
		}
		$stmt->close();
	}else{
		$error= "bad query";
		$success = 0;
	}
	return false;
}

function createTag($tag){
	global $error;
	global $success;
	global $conn;
	$sql = "INSERT INTO tags(tag_name) VALUES (?)";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_tag_name);
		$param_tag_name = $tag->name;
		
		if(!$stmt->execute()){
			$error = "bad query";
			$success = 0;
		}
		$stmt->close();
	}else{
		$error= "bad query: ";
		$success = 0;
	}
}

function isTagCreated($tag){
	global $error;
	global $success;
	global $conn;
	$sql = "SELECT tag_name FROM tags WHERE tag_name LIKE ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_tag_name);
		$param_tag_name = $tag->name;
		if(!$stmt->execute()){
			$error = "bad query";
			$success = 0;
		}
		$stmt->store_result();
		if($stmt->num_rows() > 0){
			return true;
		}	
		$stmt->close();							
	}else{
		$error= "bad query";
		$success = 0;
	}
	return false;
}

function createPurchase($product_id, $date, $user_id){
	global $error;
	global $success;
	global $conn;
	$sql = "INSERT INTO purchases(users_idusers, purchase_date, products_idproducts) VALUES(?, ?, ?)";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("isi", $param_user_id, $param_date, $param_product_id);
		$param_user_id = $user_id;
		$param_product_id = $product_id;
		$param_date = $date;
		
		if(!$stmt->execute()){
			$error= "bad query";
			$success = 0;
		}
		$stmt->close();
	}else{
		$error= "bad query";
		$success = 0;
	}
}

function createProductHasTags($tag_id, $product_id, $tag){
	global $error;
	global $success;
	global $conn;
	$sql = "INSERT INTO products_has_tags(products_idproducts, tags_idtags, rating, liked) VALUES (?, ?, ?, ?)";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("iiii", $param_product_id, $param_tag_id, $param_tag_rating, $param_tag_liked);
		$param_tag_id = $tag_id;
		$param_product_id = $product_id;
		$param_tag_rating = $tag->rating;
		$param_tag_liked = $tag->liked;
		
		if(!$stmt->execute()){
			$error = "bad query n";
			$success = 0;
		}
		$stmt->close();
	}else{
		$error= "bad query: 14";
		$success = 0;
	}
}

function getTagId($tag){
	global $error;
	global $success;
	global $conn;
	$sql = "SELECT idtags FROM tags WHERE tag_name LIKE ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_tag_name);
		error_log($tag->name);
		$param_tag_name = $tag->name;
		$stmt->bind_result($t_tag_id);
		if(!$stmt->execute()){
			$error = "bad query a";
			$success = 0;
		}
		$stmt->store_result();
		if($stmt->num_rows() > 0){
			$stmt->fetch();
			$tag_id = $t_tag_id;
			$stmt->close();
			return $tag_id;
		}
		$stmt->close();
		
	}else{
		$error= "bad query: 11";
		$success = 0;
	}
	return 0;
}
main();
?>
 