# Users Directory app for a test
This app download a users list,
using retrofit and rxjava,
shows their details
and also their location on a map
using the HERE sdk ( https://developer.here.com/ )
that displays Open Street Map data.

The UI is done using fragments
with material design principles in mind.

The users list is also saved in a file with serialization
so that , when offline, the list is restored
from the saved data ( deserialization )
so it's working offline provided 
you downloaded the data at least once.

Created by chevil ( chevil@giss.tv )
