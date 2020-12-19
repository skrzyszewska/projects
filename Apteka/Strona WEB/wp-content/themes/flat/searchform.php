	<form method="get" id="searchform" action="<?php echo esc_url( home_url( '/' ) ); ?>">
		<label for="s" class="assistive-text"><?php _e( 'Search', 'flat' ); ?></label>
		<input type="text" class="field" name="s" id="s" placeholder="<?php esc_attr_e( 'Wyszukaj', 'flat' ); ?>" />
		<input type="submit" class="submit" name="submit" id="searchsubmit" value="<?php esc_attr_e( 'Search', 'flat' ); ?>" />
		<label><input type="checkbox" name="sklad" value="tak" <?php if($_GET['sklad'] == 'tak') echo "checked";?> /> Szukaj również w składzie</label>
	</form>