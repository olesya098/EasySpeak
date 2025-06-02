package com.hfad.easyspeak.presentation.components

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

fun addEnglishTextsToFirebase() {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val textsRef: DatabaseReference = database.getReference("texts")

    val textsData = hashMapOf(
        "Easy" to listOf(
            hashMapOf(
                "title" to "My day",
                "author" to "...",
                "text" to "First, I wake up. Then, I get dressed. I walk to school. I do not ride a bike. I do not ride the bus. I like to go to school. It rains. I do not like rain. I eat lunch. I eat a sandwich and an apple.\n" +
                        "\n" +
                        "I play outside. I like to play. I read a book. I like to read books. I walk home. I do not like walking home. My mother cooks soup for dinner. The soup is hot. Then, I go to bed. I do not like to go to bed."
            ),
            hashMapOf(
                "title" to "My Wonderful Family",
                "author" to "Anna Brown",
                "text" to "I live in a house near the mountains. I have two brothers and one sister, and I was born last. My father teaches mathematics, and my mother is a nurse at a big hospital. My brothers are very smart and work hard in school. My sister is a nervous girl, but she is very kind. My grandmother also lives with us. She came from Italy when I was two years old. She has grown old, but she is still very strong. She cooks the best food!\n" +
                        "\n" +
                        "My family is very important to me. We do lots of things together. My brothers and I like to go on long walks in the mountains. My sister likes to cook with my grandmother. On the weekends we all play board games together. We laugh and always have a good time. I love my family very much.\n" +
                        "\n"
            ),
            hashMapOf(
                "title" to "Preparing food",
                "author" to "Anna Brown",
                "text" to "Jack was hungry. He walked to the kitchen. He got out some eggs. He took out some oil. He placed a skillet on the stove. Next, he turned on the heat. He poured the oil into the skillet. He cracked the eggs into a bowl. He stirred the eggs. Then, he poured them into the hot skillet. He waited while the eggs cooked. They cooked for two minutes. He heard them cooking. They popped in the oil.\n" +
                        "\n" +
                        "Next, Jack put the eggs on a plate. He placed the plate on the dining room table. Jack loved looking at his eggs. They looked pretty on the white plate. He sat down in the large wooden chair. He thought about the day ahead. He ate the eggs with a spoon. They were good.\n" +
                        "\n" +
                        "He washed the plate with dishwashing soap. Then, he washed the pan. He got a sponge damp. Finally, he wiped down the table. Next, Jack watched TV."
            ),
            hashMapOf(
                "title" to "The House",
                "author" to "Anna Brown",
                "text" to "Mr. and Mrs. Smith have one son and one daughter. The son's name is John. The daughter's name is Sarah.\n" +
                        "\n" +
                        "The Smiths live in a house. They have a living room. They watch TV in the living room. The father cooks food in the kitchen. They eat in the dining room. The house has two bedrooms. They sleep in the bedrooms. They keep their clothes in the closet. There is one bathroom. They brush their teeth in the bathroom.\n" +
                        "\n" +
                        "The house has a garden. John and Sarah play in the garden. They have a dog. John and Sarah like to play with the dog."
            ),

        ),
        "medium" to listOf(
            hashMapOf(
                "title" to "At school",
                "author" to "Science Magazine",
                "text" to "Lucas goes to school every day of the week. He has many subjects to go to each school day: English, art, science, mathematics, gym, and history. His mother packs a big backpack full of books and lunch for Lucas.\n" +
                        "\n" +
                        "His first class is English, and he likes that teacher very much. His English teacher says that he is a good pupil, which Lucas knows means that she thinks he is a good student.\n" +
                        "\n" +
                        "His next class is art. He draws on paper with crayons and pencils and sometimes uses a ruler. Lucas likes art. It is his favorite class.\n" +
                        "\n" +
                        "His third class is science. This class is very hard for Lucas to figure out, but he gets to work with his classmates a lot, which he likes to do. His friend, Kyle, works with Lucas in science class, and they have fun.\n" +
                        "\n" +
                        "Then Lucas gets his break for lunch. He sits with Kyle while he eats. The principal, or the headmaster as some call him, likes to walk around and talk to students during lunch to check that they are all behaving.\n" +
                        "\n" +
                        "The next class is mathematics, which most of the students just call math. Kyle has trouble getting a good grade in mathematics, but the teacher is very nice and helpful.\n" +
                        "\n" +
                        "His fourth class is gym. It is just exercising.\n" +
                        "\n" +
                        "History is his last class of the day. Lucas has a hard time staying awake. Many lessons are boring, and he is very tired after doing gym.\n"
            ),
            hashMapOf(
                "title" to "Days of the week",
                "author" to "Anna Brown",
                "text" to "There are seven days of the week, or uniquely named 24-hour periods designed to provide scheduling context and make time more easily measureable. Each of these days is identifiable by specific plans, moods, and tones.\n" +
                        "\n" +
                        "Monday is viewed by many to be the \"worst\" day of the week, as it marks the return to work following the weekend, when most full-time employees are given two days off. Most students attend school in the morning and return home in the afternoon (usually from about eight until three or seven until two), and most workers go to work in the morning and return home in the evening (usually from nine to five or eight to four).\n" +
                        "\n" +
                        "Tuesday is the second day of the week, and is in many ways similar to Monday. Not a whole lot of changes, schedule-wise, between Tuesday and Monday; most individuals go to school or work and return home to watch television, play video games, make plans with friends, spend time with family, read, or engage in a similar leisure-related activity.\n" +
                        "\n" +
                        "Wednesday is the third day of the week, and serves as the \"middle\" of the work week; some individuals refer to Wednesday as \"hump day,\" as once its workday is complete, employees will have passed the work-week \"hump,\" and will be on the downturn, as only two days on the job will remain in the week.\n" +
                        "\n" +
                        "Thursday is the fourth day of the week, and is viewed favorably by many, as it's rather close to the end of the work week.\n" +
                        "\n" +
                        "Friday is the fifth day of the week, and marks the end of the workweek and school-week for the vast majority of employees and students. By Friday afternoon/evening, most students/workers cannot wait to leave and go home, as they won't have to report back to school/work until Monday.\n" +
                        "\n" +
                        "Saturday is perhaps the most highly regarded day of the week. Because Sunday follows it (and there is presumably no work or school to attend, for most individuals), everyone is free to stay out (or awake) until late at night, having fun with plans or other leisure-related activities. To be sure, Saturday is generally thought of as a day to partake in hobbies that couldn't otherwise be enjoyed during the regular week.\n" +
                        "\n" +
                        "Sunday is the final day of the week, and is used by most as a day of rest. Fewer late-night plans are made on Sundays, compared to Saturdays, as most individuals have to wake up for work or school on Monday morning."
            ),

        ),
        "uneasy" to listOf(
            hashMapOf(
                "title" to "Chicago",
                "author" to "Environmental Journal",
                "text" to "Keith recently came back from a trip to Chicago, Illinois. This midwestern metropolis is found along the shore of Lake Michigan. During his visit, Keith spent a lot of time exploring the city to visit important landmarks and monuments.\n" +
                        "\n" +
                        "Keith loves baseball, and he made sure to take a visit to Wrigley Field. Not only did he take a tour of this spectacular stadium, but he also got to watch a Chicago Cubs game. In the stadium, Keith and the other fans cheered for the Cubs. Keith was happy that the Cubs won with a score of 5-4.\n" +
                        "\n" +
                        "Chicago has many historic places to visit. Keith found the Chicago Water Tower impressive as it is one of the few remaining landmarks to have survived the Great Chicago Fire of 1871. Keith also took a walk through Jackson Park, a great outdoor space that hosted the World’s Fair of 1892. The park is great for a leisurely stroll, and it still features some of the original architecture and replicas of monuments that were featured in the World’s Fair.\n" +
                        "\n" +
                        "During the last part of his visit, Keith managed to climb the stairs inside of the Willis Tower, a 110-story skyscraper. Despite the challenge of climbing the many flights of stairs, Keith felt that reaching the top was worth the effort. From the rooftop, Keith received a gorgeous view of the city’s skyline with Lake Michigan in the background."
            ),
            hashMapOf(
                "title" to "London",
                "author" to "Anna Brown",
                "text" to "London is a famous and historic city. It is the capital of England in the United Kingdom. The city is quite popular for international tourism because London is home to one of the oldest-standing monarchies in the western hemisphere. Rita and Joanne recently traveled to London. They were very excited for their trip because this was their first journey overseas from the United States.\n" +
                        "\n" +
                        "Among the popular sights that Rita and Joanne visited are Big Ben, Buckingham Palace, and the London Eye. Big Ben is one of London’s most famous monuments. It is a large clock tower located at the northern end of Westminster Palace. The clock tower is 96 meters tall. Unfortunately, Rita and Joanne were only able to view the tower from the outside. The women learned that the tower’s interior is undergoing renovations until 2021.\n" +
                        "\n" +
                        "Fortunately, the London Eye, the city’s famous Ferris wheel, was open to the public. The London Eye is situated along the southern shores of the Thames River. This attraction stands 135 meters high. It is one of London’s most well-known spots for gaining aerial views of the city. Each capsule of the Ferris wheel can hold up to 25 passengers. When their capsule stopped at the top of the Ferris wheel, the women took spectacular panoramic photographs of the beautiful cityscape below.\n" +
                        "\n" +
                        "The last place that Rita and Joanne visited was Buckingham Palace, the home of the Queen of England. The women were impressed by the palace’s incredible architecture and historical value. Both Rita and Joanne enjoyed watching the Queen’s guards outside the palace. These guards wore red tunic uniforms, shiny black boots, and bearskin hats. Despite the women’s attempts to catch the attention of the guards, the guards are specifically trained to avoid distractions. Because of this, the guards ignored the women completely.\n" +
                        "\n" +
                        "Joanne and Rita had an amazing time visiting the city of London, and they are inspired to seek more international travel destinations in the future."
            ),
            hashMapOf(
                "title" to "Jobs and Professions",
                "author" to "Anna Brown",
                "text" to "As has been the case for many years, jobs, or forms of employment wherein employees perform a service or duty in exchange for financial compensation, play a prominent role in society. Furthermore, all jobs—even those of seemingly little significance—are important, as they simply wouldn't exist if their specific responsibilities weren't of value to employers (companies or persons that pay others for their work), customers (individuals who pay money for a product or service), and the economy generally.\n" +
                        "\n" +
                        "Teachers, or educational professionals tasked with helping students understand certain subjects and topics, are especially crucial today. In short, teachers help their students to become qualified for their future careers.\n" +
                        "\n" +
                        "Doctors, or medical professionals who specialize in providing health-related assistance to patients, are some of the most respected individuals in America and the world. It's the responsibility of doctors to help those who feel less-than-stellar to determine the underlying health issue(s) and recommend an effective treatment (or remedy to a disease, disorder, or condition).\n" +
                        "\n" +
                        "There are quite a few types of specialty doctors in America (besides MD, which simply means \"medical doctor\"), all of whom can be referred to simply as \"Doctor (Name).\" Dentists (mouth/teeth doctors), dermatologists (skin doctors), and psychiatrists (mental-health doctors) are just a few examples of the many different types of doctors.\n" +
                        "\n" +
                        "Additionally, nurses are medical professionals who help to administer doctor-ordered treatments to patients.\n" +
                        "\n" +
                        "Police officers are law enforcement professionals whose job it is to protect citizens, solve crimes, and assure that rules and regulations are followed. Similarly, firefighters serve the public by responding to fires (and other emergency situations) and using high-tech equipment to extinguish these fires, while bringing any individuals who're in danger to safety.\n" +
                        "\n" +
                        "Farmers maintain fields of crops (or vegetable/fruit plants) and/or collections of animals with the intention of selling these products as food.\n" +
                        "\n" +
                        "Chefs/cooks prepare meals in professional settings, including restaurants, cafeterias, and other venues wherein food and drink are sold, for customers. Chefs are generally experienced in cooking and managing kitchens.\n" +
                        "\n" +
                        "Waiters bring menus, beverages, meals, and ultimately, the check (or a bill of the foods and drinks purchased in a transaction) to tables in restaurants and other establishments that serve food.\n" +
                        "\n" +
                        "Artists produce art, or works of creative significance, including music, paintings, drawings, poetry, writing, and more."
            ),

        )
    )

    textsRef.setValue(textsData)
        .addOnSuccessListener {
            println("Texts successfully written to Firebase!")
        }
        .addOnFailureListener { e ->
            println("Error writing texts to Firebase: ${e.message}")
        }
}