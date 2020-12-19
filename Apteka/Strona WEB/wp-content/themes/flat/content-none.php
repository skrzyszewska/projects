<h1 class="page-title"><?php _e( 'Brak wyników. Spróbuj innego słowa kluczowego:', 'flat' ); get_search_form();?></h1>

<div class="page-content">
	<?php if ( is_home() && current_user_can( 'publish_posts' ) ) : ?>

	<p><?php printf( __( 'Ready to publish your first post? <a href="%1$s">Get started here</a>.', 'flat' ), admin_url( 'post-new.php' ) ); ?></p>

	<?php elseif ( is_search() ) : ?>



	<?php else : ?>

	<p><?php _e( 'It seems we can&rsquo;t find what you&rsquo;re looking for. Perhaps searching can help.', 'flat' ); ?></p>
	<?php get_search_form(); ?>

	<?php endif; ?>
</div><!-- .page-content -->
