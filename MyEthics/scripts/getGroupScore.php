 <?php
require_once("config.php");

class GroupTag{
	public $name = "";
	public $rating = 0;
	public $liked = 0;
	
	function __construct($n, $r, $l){
		$this->name = $n;
		$this->rating = $r;
		$this->liked = $l;
	}
}

$error = "";
$tags = array();
$success = 1;
$purchase_tag_ids = array();
function main(){
	global $error;
	global $success;
	global $conn;
	global $tags;
	$purchases;
	$group_id;
	$score = 0;
	if($_SERVER["REQUEST_METHOD"] == "POST"){
		
		if(!empty($_POST["group_id"])){
			$group_id = intval(trim($_POST["group_id"]));
		}else{
			$success = 0;
			$error = "No product id.";
		}
		if(!empty($_POST["purchases"])){
			error_log($_POST["purchases"]);
			$purchases = json_decode($_POST["purchases"]);
		}else{
			$success = 0;
			$error = "No purchases.";
		}
		
		if($success == 1){ 
			foreach($purchases as $purchase){
				getProductTags($purchase->id);
			}
			$score = getTagsFromGroupId($group_id);
		}
		
		
		$conn->close();
	}else{
		$success = 0;
		$error = "No post";
	}

	$data = ["success" => $success, "error" =>$error, "score"=>$score];

	header("Content-Type:application/json");
	echo json_encode($data);
}

function getTagsFromGroupId($group_id){
	global $error;
	global $conn;
	global $success;
	global $purchase_tag_ids;
	$group_score = 0;
	$sql = "SELECT tags_idtags FROM groups_has_tags WHERE groups_idgroups = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("i", $param_group_id);
		$param_group_id = $group_id;
		if($stmt->execute()){
			$stmt->store_result();
			if($stmt->num_rows() > 0){
				$stmt->bind_result($t_tag_id);
				while($stmt->fetch()){
					foreach($purchase_tag_ids as $purchase_tag_id){
						if($purchase_tag_id == $t_tag_id){
							$group_score += getTagScore($t_tag_id);
						}
					}
				}
					
			}else{
				$success = 0;
				$error = "No group has tags.";
			}
		}else{
			$success = 0;
			$error = "no results";
		}
		$stmt->close();
	}else{
		$success = 0;
		$error = "bad query 2";
	}
	return $group_score;
}

function getTagScore($tag_id){
	global $error;
	global $success;
	global $conn;
	$sum_rating = 0;
	$i = 0;
	
	$sql = "SELECT rating, liked FROM products_has_tags WHERE tags_idtags = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("i", $param_tag_id);
		$param_tag_id = $tag_id;
		if($stmt->execute()){
			$stmt->store_result();
			if($stmt->num_rows() > 0){
				$stmt->bind_result($t_tag_rating, $t_tag_liked);
				while($stmt->fetch()){
					if($t_tag_liked == 1){
						$sum_rating += $t_tag_rating;
					}else{
						$sum_rating -= $t_tag_rating;
					}
					$i++;
				}
				
			}else{
				$success = 0;
				$error = "No purchase has tags.";
			}
		}else{
			$success = 0;
			$error = "no results";
		}
		$stmt->close();
	}else{
		$success = 0;
		$error = "bad query 2";
	}
	return $sum_rating;
}

function getProductTags($purchase_id){
	global $error;
	global $success;
	global $conn;
	global $purchase_tag_ids;
	$sql = "SELECT products_idproducts FROM purchases WHERE idpurchases = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("i", $param_product_id);
		$param_product_id = $purchase_id;
		if($stmt->execute()){
			$stmt->store_result();
			if($stmt->num_rows() > 0){
				$stmt->bind_result($t_product_id);
				while($stmt->fetch()){
					$purchase_tags = array_push($purchase_tag_ids, getTagIdFromProductId($t_product_id));
				}
			}else{
				$success = 0;
				$error = "No purchase has tags.";
			}
		}else{
			$success = 0;
			$error = "no results";
		}
		$stmt->close();
	}else{
		$success = 0;
		$error = "bad query 2";
	}
}

function getTagIdFromProductId($product_id){
	global $error;
	global $success;
	global $conn;
	$sql = "SELECT tags_idtags FROM products_has_tags WHERE products_idproducts = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("i", $param_product_id);
		$param_product_id = $product_id;
		if($stmt->execute()){
			$stmt->store_result();
			error_log($stmt->num_rows());
			if($stmt->num_rows() > 0){
				$stmt->bind_result($t_tag_id);
				while($stmt->fetch()){
					return $t_tag_id;
				}
			}else{
				$success = 0;
				$error = "No purchase has tags.";
			}
		}else{
			$success = 0;
			$error = "no results";
		}
		$stmt->close();
	}else{
		$success = 0;
		$error = "bad query 2";
	}
	return 0;
}

main();
?>
