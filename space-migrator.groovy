/**
 * Confluence Space Migrator
 *
 * Usage instructions can be found in readme.md
 */

// User Mapping

def replace = [:]
def lines = 0
def replaces = 0
new File("user-mappings.csv").eachLine { String mapping ->
  lines++
  if (mapping) {
    def user = mapping.split(';')
    if (user.size() > 1) {
      replace[user[0]] = user[1]
      replaces++
    } else {
      replace[user[0]] = 'old_' + user[0]
    }
  }
}
println "Processed ${lines} lines."
println "User mappings for ${replaces} user found."

// Migration Work

def file = getUserInput('Select entities.xml File: ', { new File(it).exists() })
def errors = 0

new File("${file}.migrated").withWriter('UTF-8') { out ->
  def isUser = false
  new File(file).eachLine('UTF-8') { String line ->
    if (line == '<object class="ConfluenceUserImpl" package="com.atlassian.confluence.user">') {
      isUser = true
    }
    if (isUser) {
      def matcher = (line =~ /<property name=".*"><!\[CDATA\[(.*)\]\]><\/property>/)
      if (matcher.find()) {
        def username = matcher.group(1)
        def replaceBy = replace.get(username)
        if (!replaceBy) {
          println "No replacement found for '$username'"
          errors++
          out.println line
        } else {
          out.println line.replace(username, replaceBy)
        }
      } else {
        out.println line
      }
    } else {
      out.println line
    }
    if (line == '</object>') {
      isUser = false
    }
  }
}

// Results

println "Finished with ${errors} errors!"

// Helper Methods

def getUserInput(text, validation = { true }) {
  print text
  def value = ''
  while (!value || !validation(value)) {
    value = new Scanner(System.in).nextLine()
    if (!validation(value)) {
      print "Value '${value}' is not a valid input: "
    }
  }
  value
}
