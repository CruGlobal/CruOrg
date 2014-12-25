require 'compass/import-once/activate'
# Require any additional compass plugins here.

# Set this to the root of your project when deployed:
http_path = "/"
css_dir = "../../../components/page/htmlbase/clientlibs/css"
sass_dir = "scss"
images_dir = "images"
javascripts_dir = "js"

# on success
on_stylesheet_saved do |filename|
	%x(terminal-notifier -title "Success" -message "sass-main -- #{File.basename(filename)}")
end

# on save error
on_stylesheet_error do |filename, message|
	%x(terminal-notifier -title "Fail" -message "sass-main -- #{File.basename(filename)}: #{message}")
end


# You can select your preferred output style here (can be overridden via the command line):
# output_style = :expanded or :nested or :compact or :compressed

# To enable relative paths to assets via compass helper functions. Uncomment:
# relative_assets = true

# To disable debugging comments that display the original location of your selectors. Uncomment:
# line_comments = false