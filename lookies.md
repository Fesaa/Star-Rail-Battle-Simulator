# Some stuff to look into

Small thinks that are not working correctly, that I should look into

### Investigate some more dmg differences / set up some tests
There are still some battles that have different dmg outcomes, but the more code changes,
the more these differences are from actual bug fixes. It's the to set a baseline, add tests.
And when they fail, investigate and make a single commit with changes to test & the bug fix

### Add back break, add it properly
Add break dmg from Ruan Mei back, add break dmg when toughness is depleted. And add extra delay on imaginary break

### Investigate enemy dmg
I'm currently uses homdgcat's wiki dmg; But this is for 1000 DEF. But I can't figure out where 
any of the numbers below are coming from. And hakush doesn't list any.
I'm just confused...

### Investigate enemies in general
They scale all their stats off something. But I don't know what at the moment.
Need to check, so we can make them all dynamic. Or something. Not sure.


### Look into actual game data to construct stages
dim's data dump wiki

### Compare some logs from my sanity dumb
There are a lot of changes, which makes sense. As I fixed a ton of small bugs along
the way while working on the latest Attack reword. But just to double-check, also
has to rewrite the same nonsense again, so ya.