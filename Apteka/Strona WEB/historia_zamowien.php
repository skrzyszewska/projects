<?php
	require('htmlTablePDF.php');
	define( 'SHORTINIT', true );
	require_once( $_SERVER['DOCUMENT_ROOT'] . '/wp-load.php' );
	global $wpdb;
    if($_SERVER['REQUEST_METHOD'] != "POST") {
		echo "Nie masz uprawnień do przeglądania tej strony.";
		return;
	}
	$pdf=new PDF();
	$pdf->AddPage();
	
	$pdf->AddFont('arial_ce','','arial_ce.php');
	$pdf->AddFont('arial_ce','I','arial_ce_i.php');
	$pdf->AddFont('arial_ce','B','arial_ce_b.php');
	$pdf->AddFont('arial_ce','BI','arial_ce_bi.php');
	
	$pdf->SetFont('arial_ce','B',20);
	
	$date = new DateTime(date("Y-m-d"));
	
	//$okres_tab['dzisiaj'] = $date->modify('-0 day');
	$okres_tab['dzisiaj'] = '-0 day';
	$okres_tab['tydzien'] = '-1 week';
	$okres_tab['miesiac'] = '-1 month';
	$okres_tab['rok'] = '-1 year';
	$okres_tab['wszystkie'] = '-20 year';

	$okres = $_POST['okres'];

	$status_tab['zrealizowane'] = 'Zrealizowane';
	$status_tab['odrzucone'] = 'Odrzucone';
	$status_tab['oczekujace'] = 'Oczekuje na akceptacje';
	$status_tab['wszystkie'] = '%';
	
	$status_show_tab['zrealizowane'] = 'zrealizowanych';
	$status_show_tab['odrzucone'] = 'odrzuconych';
	$status_show_tab['oczekujace'] = 'oczekujących';
	$status_show_tab['wszystkie'] = 'wszystkich';
	
	$status = $status_tab[$_POST['status']];
	
	$data_od = $date->modify($okres_tab[$okres]);
	$data_od = $data_od->format('Y-m-d');
	$data_do = date("Y-m-d"); 
	
	$pikam_tekst = "wp-content/uploads/Images/pikam.png";
	$pikam_logo = "wp-content/uploads/Images/Logo.png";
	$pdf->Image($pikam_logo, 90, null, 33.78);
	$pdf->Image($pikam_tekst, 50, null);
	
	
	if($okres == "wszystkie")
		$tekst = "Historia ".$status_show_tab[$_POST['status']]." zamówień do dnia $data_do.";
	else
		$tekst = "Historia ".$status_show_tab[$_POST['status']]." zamówień z dni $data_od - $data_do.";
	
	$pdf->MultiCell(200,10, Pl($tekst), 0, 'C', 0);
	$pdf->WriteHTML("<br><br>");

	$query = "FROM HistoriaZamowien WHERE Data >= '$data_od' AND Data <= '$data_do' AND Status LIKE '$status'";
	$count = $wpdb->get_var("SELECT COUNT(*) ".$query);
	if($count == 0) {
		$pdf->MultiCell(200,100, Pl("Brak zamówień w historii o podanych kryteriach."), 0, 'C', 0);
		$pdf->Output();
	}
	$query = "SELECT * ".$query." ORDER BY Data DESC";
	$przychod = 0;
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
	$pdf->SetFont('arial_ce','I',12);
	$pdf->WriteHTML(Pl("<br>Ilosć zamówień: ".count($tablica_duza)."<br><br><br>"));
	$pdf->SetFont('arial_ce','',12);
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
			<td bgcolor="#998E8E" align="CENTER" width="30">Nr.</td>
			<td bgcolor="#998E8E" align="CENTER" width="200">Nazwa</td>
			<td bgcolor="#998E8E" align="CENTER" width="80">Ilosć</td>
			<td bgcolor="#998E8E" align="CENTER" width="150">Cena za sztukę</td>
			<td bgcolor="#998E8E" align="CENTER" width="100">Cena razem</td>
			<td bgcolor="#998E8E" align="CENTER" width="100">Na receptę</td>
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
		$pdf->WriteHTML(Pl("Wartosć zamówienia: ".$do_zaplaty."zł<br><br><hr><br>"));
		$przychod = $przychod + $do_zaplaty;
	}
	$pdf->SetFont('arial_ce','I',12);
	$pdf->SetTextColor(100,100,100);
	$pdf->WriteHTML(Pl("<br><br>Ogólny przychód z danego okresu: ".$przychod."zł<br><br><hr><br>"));
	$pdf->Image($pikam_logo, 90, null, 33.78);
	$pdf->Image($pikam_tekst, 50, null);
	$pdf->Output();

    function Pl($tekst) {
		return iconv('UTF-8','iso-8859-2//TRANSLIT//IGNORE',$tekst);
	}
?>