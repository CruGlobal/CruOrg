# notification :off

watch(%r{^CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/scss(.+)\.scss$}) do |m|

    # build ie version of changed file in scss-main
    scss_ie_file = 'CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/scss/main' + m[1] + '.scss'

    puts scss_ie_file
    puts m[0]
    puts m[1]

    # copy scss-main to scss-ie
    system "cp #{m[0]} #{scss_ie_file}"

    # terminal notification
    %x(terminal-notifier -title "File copied" -message "#{m[1]}.scss -- scss-ie")
end