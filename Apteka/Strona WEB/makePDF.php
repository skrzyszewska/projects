<?php

	require('htmlTablePDF.php');
	define( 'SHORTINIT', true );
	require_once( $_SERVER['DOCUMENT_ROOT'] . '/wp-load.php' );
	global $wpdb;

	$pdf=new PDF();
	$pdf->AddPage();
	
	$pdf->AddFont('arial_ce','','arial_ce.php');
	$pdf->AddFont('arial_ce','I','arial_ce_i.php');
	$pdf->AddFont('arial_ce','B','arial_ce_b.php');
	$pdf->AddFont('arial_ce','BI','arial_ce_bi.php');
	
	$pdf->SetFont('arial_ce','B',20);


	
	$data_od =  $_POST['data_od'];
	$data_do =  $_POST['data_do'];
	
	$data_od = "2018-05-29";
	$data_do = date("Y-m-d"); 
	
	$tekst = "Historia zamówień z dni $data_od - $data_do";
	$tekst = iconv('UTF-8','iso-8859-2//TRANSLIT//IGNORE',$tekst);

	
	$pdf->MultiCell(200,5, $tekst, 0, 'C', 0);
	$pdf->WriteHTML("<br><br>");
	if($wpdb->get_var("SELECT COUNT(*) FROM HistoriaZamowien WHERE Data >= '$data_od' AND Data <= '$data_do'") == 0) {
		$pdf->WriteHTML("Brak zamówień w historii.");
	}
	
	$pdf->SetFont('arial_ce','',12);
	//$query = "SELECT * FROM HistoriaZamowien WHERE Data >= '$data_od' AND Data <= '$data_do'";
	$query = "SELECT * FROM HistoriaZamowien WHERE Data >= '$data_od' AND Data <= '$data_do' ORDER BY Data DESC";
    foreach( $wpdb->get_results($query) as $key => $row) { 
		 $tablica_mala["klient_id"] = $row->KlientID; 
         $tablica_mala["lek_id"] = $row->LekID;  
         $tablica_mala["cena"] = $row->Cena;  
         $tablica_mala["ilosc"] = $row->Ilosc;  
         $tablica_mala["data"] = $row->Data;
         $tablica_mala["status"] = $row->Status;
         $tablica_mala["powod"] = $row->Komentarz;
         $tablica_mala["koszyk_id"] = $row->KoszykID;
         $tablica_mala["plik"] = $row->ReceptaPlik;
         $tablica_duza[$row->KoszykID][] = $tablica_mala; 
    }
	foreach( $tablica_duza as $klucz => $wartosc) {
		
		$pdf->SetFont('arial_ce','B',12);
		$klient = $wpdb->get_row( "SELECT * FROM Klienci WHERE ID = ".$wartosc[0][klient_id] );
		$pdf->WriteHTML(Pl("Użytkownik: ".$klient->Login."<br>"));
		$pdf->WriteHTML(Pl("Status zamówienia: "));
		
		if($wartosc[0][status] == "Odrzucone") {
			$pdf->SetTextColor(255,0,0);
			$pdf->WriteHTML(Pl($wartosc[0][status]."<br>"));
			$pdf->SetTextColor(0,0,0);
			$pdf->WriteHTML(Pl("Powód odrzucenia: <br>"));
			$pdf->SetFont('arial_ce','',12);
			$pdf->SetTextColor(255,0,0);
			$pdf->WriteHTML(Pl($wartosc[0][powod]."<br>"));
			$pdf->SetFont('arial_ce','B',12);
			$pdf->SetTextColor(0,0,0);
		} elseif ($wartosc[0][status] == "Zrealizowane"){
			$pdf->SetTextColor(0,255,0);
			$pdf->WriteHTML(Pl($wartosc[0][status]."<br>"));
			$pdf->SetTextColor(0,0,0);
		} else {
			$pdf->WriteHTML(Pl($wartosc[0][status]."<br>"));
		}
		$pdf->WriteHTML(Pl("Data zamówienia: ".$wartosc[0][data]."<br><br>"));
		$pdf->SetFont('arial_ce','',12);
		$html='<table border="1">
		<tr>
			<td bgcolor="#61575C" align="CENTER" width="30">Nr.</td>
			<td bgcolor="#61575C" align="CENTER" width="80">Zdjęcie</td>
			<td bgcolor="#61575C" align="CENTER" width="200">Nazwa</td>
			<td bgcolor="#61575C" align="CENTER" width="80">Ilosć</td>
			<td bgcolor="#61575C" align="CENTER" width="150">Cena za sztukę</td>
			<td bgcolor="#61575C" align="CENTER" width="100">Cena razem</td>
			<td bgcolor="#61575C" align="CENTER" width="100">Na receptę</td>
		</tr>';
		$pdf->WriteHTML(Pl($html));
		$nr = 1;
		$do_zaplaty = 0;
		foreach( $wartosc as $key => $row) {
			
            $lek = $wpdb->get_row( "SELECT * FROM Leki WHERE ID = $row[lek_id]" );
			$cena_razem = $row[cena]*$row[ilosc];
			$na_recepte = ucfirst(strtolower($lek->NaRecepte));
			$html='
			<tr>
				<td align="CENTER" width="30">'.$nr.'</td>
				<td align="CENTER" width="80">Zdjęcie</td>
				<td align="CENTER" width="200">'.$lek->Nazwa.'</td>
				<td align="CENTER" width="80">'.$row[ilosc].'</td>
				<td align="CENTER" width="150">'.$row[cena].'zł</td>
				<td align="CENTER" width="100">'.$cena_razem.'zł</td>
				<td align="CENTER" width="100">'.$na_recepte.'</td>
			</tr>';
			 $pdf->WriteHTML(Pl($html));
			 $nr = $nr + 1;
			 $do_zaplaty = $do_zaplaty + $cena_razem;
		}
		$pdf->WriteHTML('</table><br>');
		$pdf->SetFont('arial_ce','B',12);
		$pdf->WriteHTML(Pl("Razem: ".$do_zaplaty."zł<br><br><hr>"));
	}
	
	$pdf->Output();


    function Pl($tekst) {
		return iconv('UTF-8','iso-8859-2//TRANSLIT//IGNORE',$tekst);
	}
?>