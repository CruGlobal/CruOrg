notification :off

watch(%r{^CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-main/scss(.+)\.scss$}) do |m|
    # build ie version of changed file in scss-main
    scss_ie_file = 'CQFiles/CruOrgApp/@JCR_ROOT/apps/CruOrgApp/static/sassfiles/scss-ie/scss/main' + m[1] + '.scss'

    # copy scss-main to scss-ie
    if system "cp #{m[0]} #{scss_ie_file}"
        %x(terminal-notifier -title "File copied" -message "#{m[1]}.scss -- scss-ie")
    else
        %x(terminal-notifier -title "File NOT copied" -message "#{m[1]}.scss -- FAILED")
        %x(terminal-notifier -title "Notice" -message "Please copy #{m[1]}.scss manually to scss-ie")
    end
end