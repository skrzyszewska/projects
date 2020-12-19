<?php
/**
 * The base configuration for WordPress
 *
 * The wp-config.php creation script uses this file during the
 * installation. You don't have to use the web site, you can
 * copy this file to "wp-config.php" and fill in the values.
 *
 * This file contains the following configurations:
 *
 * * MySQL settings
 * * Secret keys
 * * Database table prefix
 * * ABSPATH
 *
 * @link https://codex.wordpress.org/Editing_wp-config.php
 *
 * @package WordPress
 */

// ** MySQL settings - You can get this info from your web host ** //
/** The name of the database for WordPress */
define('DB_NAME', 'pudwelp');

/** MySQL database username */
define('DB_USER', 'pudwelp');

/** MySQL database password */
define('DB_PASSWORD', 'Apteka2017');

/** MySQL hostname */
define('DB_HOST', '212.182.24.105:3306');

/** Database Charset to use in creating database tables. */
define('DB_CHARSET', 'utf8mb4');

/** The Database Collate type. Don't change this if in doubt. */
define('DB_COLLATE', '');

/**#@+
 * Authentication Unique Keys and Salts.
 *
 * Change these to different unique phrases!
 * You can generate these using the {@link https://api.wordpress.org/secret-key/1.1/salt/ WordPress.org secret-key service}
 * You can change these at any point in time to invalidate all existing cookies. This will force all users to have to log in again.
 *
 * @since 2.6.0
 */
define('AUTH_KEY',         'S4@+]7L9HT]jQ)3!WsU*WhEu^Gv`Z9W$C2HlanIn}9r_1I!kdh}~KHkR>GM95WyI');
define('SECURE_AUTH_KEY',  ');RA&84c%wPV *`38Yvk%iY#QQ>:FXkVYUix^3Gb|o2nVaKhm@pm;bNU[*lYo-yX');
define('LOGGED_IN_KEY',    '(FpamX0aLgjhAE(c5g?skh.D5;w0(e!c|^U.lYp-H6}tK$gCFskGW4k0Om={2c6:');
define('NONCE_KEY',        ')Q&k%Cx|rvjRk.OB#m$~M*dFeLcnaj@w$$c7kvT7*=p5)i%p[.U@d:39Oqx/?%Ll');
define('AUTH_SALT',        'cbyH5${JY(Ilrq0l+fr^U3&W994?-4N#9V, &1Q#|x`-i{<r=[ :_h`wDEmOT<H*');
define('SECURE_AUTH_SALT', 'g7{qObj(#ZLU`c~DRuMa9(t1f2RH.=c7i9/Ia:~yG9SDg@%k>nOWz&=Q}!IX3m+b');
define('LOGGED_IN_SALT',   'ai~18<,v1^WuMMQ5[ zLlNny 5lc|i07oW9{$.ME}HfWbG%/T|KP-s5gwk_`HH1c');
define('NONCE_SALT',       '/aYVJNW)AgkD3a(vH&QwB#hwj3{IW(`]&>;BrxH/a8~n<1UEA2~VS4w?u(9b66b6');

/**#@-*/

/**
 * WordPress Database Table prefix.
 *
 * You can have multiple installations in one database if you give each
 * a unique prefix. Only numbers, letters, and underscores please!
 */
$table_prefix  = 'wp_';

/**
 * For developers: WordPress debugging mode.
 *
 * Change this to true to enable the display of notices during development.
 * It is strongly recommended that plugin and theme developers use WP_DEBUG
 * in their development environments.
 *
 * For information on other constants that can be used for debugging,
 * visit the Codex.
 *
 * @link https://codex.wordpress.org/Debugging_in_WordPress
 */
define('WP_DEBUG', false);

/* That's all, stop editing! Happy blogging. */

/** Absolute path to the WordPress directory. */
if ( !defined('ABSPATH') )
	define('ABSPATH', dirname(__FILE__) . '/');

/** Sets up WordPress vars and included files. */
require_once(ABSPATH . 'wp-settings.php');
