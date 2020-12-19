<?php
/*
Plugin Name: Hide Entry Title
Plugin URI: http://seosthemes.com/hide-entry-title/
Description: Simple WordPress hide entry title Plugin. Hide Entry Title is easy to use. Hide the title on all pages and posts 
Version: 1.0
Contributors: seosbg
Author: seosbg
Author URI: http://seosthemes.com/
Text Domain: hide-entry-title
*/


// ************* User Section **************

add_action('admin_menu', 'hide_entry_title_menu');

function hide_entry_title_menu() {
	add_menu_page('Hide Entry Title', 'Hide Entry Title', 'administrator', 'hide-entry-title-settings', 'hide_entry_title_plugin_settings_page', plugins_url( 'images/icon.png' , __FILE__  ));
}

add_action( 'admin_init', 'hide_entry_title_plugin_settings' );

function hide_entry_title_plugin_settings() {
	register_setting( 'hide-entry-title-settings-group', 'hide_entry_title' );
}
	
/*********************** Admin Scripts and Styles **************************/

function hide_entry_title_admin_scripts() {
	wp_enqueue_style( 'hide-entry-title-admin-css', plugin_dir_url(__FILE__) . '/css/admin.css' );
}
 
add_action( 'admin_enqueue_scripts', 'hide_entry_title_admin_scripts' );	


function hide_entry_title_plugin_settings_page() {
?>

<form action="options.php" method="post" role="form" name="font-size-form">

	<?php settings_fields( 'hide-entry-title-settings-group' ); ?>
	<?php do_settings_sections( 'hide-entry-title-settings-group' ); ?>
			
	<div class="hide-entry-title">

		<div class="hide-entry-title-seos">
			<div>
				<a target="_blank" href="https://seosthemes.com/">
					<div class="btn s-red">
						 <?php _e('SEOS ', 'hide-entry-title'); echo ' <img class="ss-logo" src="' . plugins_url( 'images/logo.png' , __FILE__ ) . '" alt="logo" />';  _e(' THEMES', 'hide-entry-title'); ?>
					</div>
				</a>
			</div>
		</div>	
		
			<div class="new-opt">
				<ul>
					<li>
						<strong><?php _e('Hide Entry Title:', 'hide-entry-title'); ?></strong>
					</li>
						<?php $hide_entry_title = get_option( 'hide_entry_title' ); ?>	
					<li>
						<?php _e('Hide class <b>entry-title</b> on all pages and posts : ', 'hide-entry-title'); ?>
						<input type="checkbox" name="hide_entry_title" value="1"<?php checked( 1 == $hide_entry_title); ?> />
					</li>
				</ul>	<?php submit_button(); ?>
			</div>

	</div>
</form>
<?php }

	function hide_entry_title_language_load() {
		load_plugin_textdomain('hide_entry_title_language_load', FALSE, basename(dirname(__FILE__)) . '/languages');
	}
	add_action('init', 'hide_entry_title_language_load');
	

	if (get_option( 'hide_entry_title' ) == 1) {
		function hide_entry_title() {
			echo "<style> .entry-title { display: none !important; }</style>";
		}
		add_action('wp_head','hide_entry_title');
	}