GitHub Repo: https://github.com/wormwarrior56/SE333assignment6.git

Manual UI Tesing

Going through the website with Playwright automatically was very intuitive. Clicking an element would expose 
how Playwright was reading it, and there were several options for generating assertThat statements.
I mostly used assertThat with the text option, but the visibility option seems helpful too.
Originally I was trying to separate each testcase into its own test, hence the redundant order(1).
I was having trouble saving the state of a test and loading it in the next test, so I tried putting everything
I had at that point into one test, and it ran to completion first try, so I didn't bother trying to make more than 
one test. In this test, it makes sense to use one test anyway because the testcases all piggyback off each other.
For a Playwright testing scenario where specific states are all that are needed, such as a successful login, using 
multiple tests becomes more important, as all the tests are more likely to be independent from each other.



AI-Assisted UI Testing

The agent I used was GitHub Copilot, and the model of agent I used was GPT-5 mini. At first, I had trouble setting 
this up in VS Code, but then I realized I had created the necessary json file early on and didn't realize it.
After using >MCP:List Servers in the searchbar at the top, I saw that Playwright was a registered server. Then, 
I went to extensions and got GitHub Copilot, GitHub Copilot Chat, as well as some extensions for Java. 

It was cool having Copilot open the webpage in VS Code, but then it refused to actually access that window, saying 
the file type wasn't supported, so it ended up opening a window outside of VS Code anyway. After that, I set guidelines
for what I wanted to see from the tests:


You have access to the tool Playwright that allows you to navigate web pages.
We are using the webpage https://depaul.bncollege.com/ as a starting point.
Access this through the Simple Browser window we opened.

Use the following guidelines for tests:

-Do not make assumptions, manually navigate through the webpage for every single step
-Only assert when explicitly told to do so
-All coding done must be java, and it must execute successfully in Junit.
-Follow Playwright best practices
-Try to use many getByRole statements as seen in example
-When asked to assert something, use assertThat statements with the assert text feature in Playwright


In playwrightAITests, I have provided all imports necessary.
pom.xml is provided.
DO NOT ADD ANY MORE IMPORT STATEMENTS.
DO NOT ADD ANY STATEMENTS THAT REQUIRE CHANGING THE POM.XML IN ANY WAY.

(The instructions I gave it were just the list of instructions outlined in Part B separated with a hyphen at the beginning)

These guidelines came from trial and error and still weren't that good. For starters, the AI made assert statements after
almost every click, which was more than necessary. The AI would also go up until the filtering section and assume it had
all information necessary for the rest of the tests, which was very wrong. Even with the instruction to navigate
through every single step, it would still stop halfway through, and I would have to remind it to keep going.
I also saw that the AI uses many more locator instance than I did, and I think this was to avoid clicking
more elements than it had to. 

Because of all these factors, the accuracy was abysmal. Not counting the first couple tries that didn't even compile,
once the AI created a decent skeleton, it would have to fix almost every single statement past the filtering phase
with the color, brand, and price. In some instances, I could not get the AI to generate a working test, such as 
on lines 200 and 202-203. At that point, I just inserted the cases that worked in my code, and they also worked 
in the AI's code. Other than these couple instances, the AI was able to fix most of its mistakes albeit after much 
trial and error. 

Maintenance effort was also a struggle. The AI really didn't want to use Playwright's architecture, and I had to 
tell it to use AriaRole statements because originally it was trying to grab those words by providing a string. 
Another problem I had with maintenance was that sometimes when I gave it an error log to base a fix off, it would 
change way more than necessary and would also still not work, so I had to revert changes at least twice.

My overall impression of the AI feature is that with some learning on my end about how to prompt it, the AI could
be much more efficient than it was here. I have seen people develop extremely specific prompts and get exactly
what they want, so when using AI in conjunction with code again I will spend more time developing specific 
guidelines for the AI to follow. 


*Sidenote, there are a lot of videos generated because I was getting inconsistent results for a while.
The final run of tests came back consistently successful after a couple changes, so I left them as proof of
testing and because I don't think the GitHub actions can save both videos based on one pom.xml.