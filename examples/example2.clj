(let [res (ctx-semver ctx nil)
      status (get-in res [:console-out :status])
      oldv (get-in res [:console-out :old-version])
      newv (get-in res [:console-out :new-version])]
  (if (= status :updated)
    (str "Version updated: " oldv " -> " newv)
    "Version not updated"))
