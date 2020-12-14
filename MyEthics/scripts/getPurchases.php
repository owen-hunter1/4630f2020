<?php

class purchase{
	public $product_name = "";
	public $purchase_date = 0;
	public $id = 0;
}

require_once("config.php");

$error = "";
$success = 1;
$purchases = array();

function main(){
	global $error;
	global $success;
	global $conn;
	global $purchases;
	if($_SERVER["REQUEST_METHOD"] == "POST"){
		$user_id;
		if(!empty($_POST["user_id"])){
			$user_id = $_POST["user_id"];
		}else{
			$error = "No user id.";
			$success = 0;
		}
		
		if($success == 1){
			getPurchases($user_id);
		}
		$conn->close();
	}else{
		$error = "No post";
		$success = 0;
	}

	$data = ["success" => $success, "error" => $error, "purchases"=> $purchases];

	header("Content-Type:application/json");
	echo json_encode($data);
}
//get the product id and purchase date from purchases table 
function getPurchases($user_id){
	global $error;
	global $success;
	global $conn;
	global $purchases;

	$t_purchase;
	$sql = "SELECT products_idproducts, purchase_date FROM purchases WHERE users_idusers = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_user_id);
		$param_user_id = $user_id;
		if($stmt->execute()){
			$stmt->store_result();
			if($stmt->num_rows() > 0){
				$stmt->bind_result($t_product_id, $t_date);
				while($stmt->fetch()){
					getProductNames($t_product_id, $t_date);
				}
					
			}else{
				$error = "No purchases created yet.";
				$success = 0;
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
}

//get product name from product id
function getProductNames($product_id, $date){
	global $error;
	global $success;
	global $conn;
	global $purchases;
	$t_purchase = new Purchase();
	$t_purchase->purchase_date = intval($date);

	$sql = "SELECT product_name FROM products WHERE idproducts = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_product_id);
		$param_product_id = $product_id;
		if($stmt->execute()){
			$stmt->store_result();
			if($stmt->num_rows() > 0){
				$stmt->bind_result($t_product_name);
				$stmt->fetch();
				$t_purchase->product_name = $t_product_name;
				$t_purchase->id = $product_id;
			}
			else{
				$error = "No products created yet.";
			}
		}else{
			$error = "no results stmt";
			$success = 0;
		}
		$stmt->close();
	}else{
		$error = "bad query stmt";
		$success = 0;
	}
	array_push($purchases, $t_purchase);
	
}
main();
?>
