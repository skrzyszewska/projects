<style style="text/css">
   tr.tr-hover-class:hover {
      background: #eeeeee;
   }
</style>

<?php get_header(); ?>
	<?php flat_hook_search_before(); ?>
	<h1 class="page-title">
	<?php 
	printf( __( '<font size="6">Wynik wyszukiwania dla: <b>%s</b></font>', 'flat' ), get_search_query() ); 
	?>
	
	<?php get_search_form(); ?></h1>
	<div id="content" class="site-content" role="main">
		<?php flat_hook_search_top(); ?>
	<?php 
		$word = $_GET["s"];
		if ($_GET['sklad'] == 'tak') {
			$query = "SELECT * FROM Leki WHERE LOWER( Nazwa ) LIKE LOWER( '%$word%' ) OR LOWER ( Sklad ) LIKE LOWER ( '%$word%' )ORDER BY Nazwa ";
		}
		else {
			$query = "SELECT * FROM Leki WHERE LOWER( Nazwa ) LIKE LOWER( '%$word%' ) ORDER BY Nazwa";
		}

		$results = $wpdb->get_results($query);
		if($wpdb->num_rows > 0) {
			echo '<table width="475" border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td style="text-align: center;" width="100"><b>Zdjęcie</td>
				<td style="text-align: center;" width="150"><b>Nazwa</td>
				<td style="text-align: center;" width="265"><b>Opis</td></tr>';
			foreach( $results as $key => $row) {
				$errorPhoto = "wp-content/uploads/Images/no_photo.png";
				$photo = "wp-content/uploads/Images/Leki/$row->Zdjecie";
	
				$opisTable= explode(" ", $row->Opis);
				$opis = "";
				foreach($opisTable as $slowo) {
					if((strlen($opis)+strlen($slowo)) < 110) {
						$opis .= " ".$slowo;
					} else {
						$opis .= " (...)";
						break;
					}

				}
 				$buffor .= "<tr class='tr-hover-class'  style='cursor: pointer;' onclick=\"window.location='?page_id=195&id=$row->ID';\">";
				$buffor .= "
						  <td style=\"background: #FFFFFF !important;\"><center><img src=\"scaleImage.php/?img=$photo&width=100&length=100\" onerror=\"this.src='scaleImage.php/?img=$errorPhoto&width=100&length=100'\"></center></td>
						  <td>$row->Nazwa</td>
						  <td>$opis</td>";
				$buffor .= "</a></tr>";
				
			}
			echo $buffor;
			echo '</table><br>';
		} else {
			//get_template_part( 'content', 'none' );
		}
	?>

		<?php flat_hook_search_bottom(); ?>
	</div>
	<?php flat_hook_search_after(); ?>
<?php get_footer(); ?>
