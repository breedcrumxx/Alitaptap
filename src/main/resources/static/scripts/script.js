    class User {
        constructor(id, name, role, sex){
            this.id = id;
            this.name = name;
            this.role = role;
            this.sex = sex;
        }
    }

    class Schedule{
        constructor(Id, IntructorId, ClassId, TimeSlot, WeekSlot, SubjectCode, SubjectDescription){
            this.Id = Id;
            this.InstructorId = IntructorId;
            this.ClassId = ClassId;
            this.TimeSlot = TimeSlot;
            this.WeekSlot = WeekSlot;
            this.SubjectCode = SubjectCode;
            this.SubjectDescription = SubjectDescription;
        }
    }

    //get current state

    var currentUser;

    async function sendData(url, data){
        const response = await $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function(response){
                return response;
            }
        });

        return await response;
    }

    async function registerData(data){
        let { status, message } = await sendData("/create-account", data);
        if(status == 'success'){
            //delete all data
        }
    }

    async function openNav() {
        var form = new FormData(document.querySelector('#reg-form'));

        // console.log("sex: " + sex);
        // console.log("role: " + role);
        // return;
        const username = form.get('username');
        const fullname = form.get('fullname');
        const password = form.get('password');
        const repass = form.get('repass');
        const sex = form.get("sex");
        const role = form.get("role");

        // some inputs are empty
        if(username === "" ||
        fullname === "" ||
        password === "" ||
        repass == ""){
            document.querySelector('#error-placer').innerHTML = "Please fill in the form."; //send warning
            return;
        }

        if(password != repass){
            document.querySelector('#error-placer').innerHTML = "Password doesn't match."; //send warning
            return;
        }

        if(password.length < 8){
            document.querySelector('#error-placer').innerHTML = "Password must atleast be 8 characters long.";
            return;
        }

        let account = {
            username,
            fullname,
            password,
            sex,
            role
        };
        console.log(JSON.stringify(account));
        let { status, message } = await sendData("/api/account/create-account", account);

        if(status == "Used"){
            //username is already in used
            document.querySelector('#error-placer').innerHTML = message;
            return;
        }
        if(status == "Wait"){
            // wait for the account confirmation
            // route to login
            return;
        }

        //route to dashboard

        return;
        setTimeout(function (){
            const sb = document.querySelector('.sexBox');
            sb.className = "sexBox now-active";
            $('.curtain').append("<img src='https://i0.wp.com/neust.edu.ph/wp-content/uploads/2020/06/cropped-neust_logo-1-1.png?w=512&ssl=1' class='top-logo' alt='' style='animation: fade 3s; position: absolute; top: 30px; left: 30px; width: 80px; height: 80px;'>");
        }, 3000);

        var options = document.querySelectorAll('.sex');
        options.forEach(option => {
            option.addEventListener('click', function(){
                next(formData, this.dataset.value);
            });
        });
    }

    function draw_curtain(){
        const cur = document.querySelector('.curtain').style.left = "-1150px";
    }

    async function cover_curtain(){
        return new Promise((resolve) => {
            const cur = document.querySelector('.curtain').style.left = "0";
            setTimeout(() => {
                $('.container').remove();
                resolve();
            }, 2000);
        });
    }

    // server call
    async function getData(METHOD, URL){
        return await $.ajax({ //call to get the style sheet
            type: METHOD,
            url: URL,
            cache: false,
            success: function(data){
                return data;
            }
        });
    }

    // clicked the register now function
    async function register(){
        console.log("here")
        await cover_curtain();
        let style = await getData("GET", "/get-style/registration");
            // console.log(typeof(style));
            document.querySelector('#login-style').remove();
            document.querySelector('#style').insertAdjacentHTML("afterend", style);

        let template = await getData("GET", "/get-template/registration");
            // console.log(template);
            document.querySelector('.curtain').insertAdjacentHTML("afterend", template);

        draw_curtain();
    }

    //click the login now function
    async function login(){
        console.log("here")
        await cover_curtain();
        let style = await getData("GET", "/get-style/login");
            console.log(style);
            document.querySelector('#registration-style').remove();
            document.querySelector('#style').insertAdjacentHTML("afterend", style);

        let template = await getData("GET", "/get-template/login");
            console.log(template);
            document.querySelector('.curtain').insertAdjacentHTML("afterend", template);
        draw_curtain();
    }

    // login button function
    async function logMeIn(){
        var error = document.querySelector('#error-placer');
        var form = new FormData(document.querySelector('#log-form'));

        const username = form.get('username');
        const password = form.get('password');
    
        // some inputs are empty
        if(username === "" ||
        password === ""){
            console.log('Empty field, please fill!');
            return;
        }
    
        let formData = {
            username,
            password
        };

        let { status, message } = await sendData("/api/account/login-account", formData);
        if (status == "No match") {
            error.innerHTML = message;
            document.querySelector('#username').value = "";
            document.querySelector('#password').value = "";
            document.querySelector('#username').focus();
            return;
        }
        if(status == "Error"){
            error.innerHTML = message;
            document.querySelector('#password').value = "";
            document.querySelector('#password').focus();
            return;
        }
        
        console.log(status);

        await cover_curtain();
        
        let {id, name, role, sex} = JSON.parse(message);
        console.log("id" + id);
        currentUser = new User(id, name, role, sex);
        
        //inject dashboard template
        let style = await getData("GET", "/get-style/mainscreen");
            document.querySelector('#login-style').remove();
            document.querySelector('#style').insertAdjacentHTML("afterend", style);

        let template = await getData("GET", "/get-template/mainscreen");
            document.querySelector('.curtain').insertAdjacentHTML("afterend", template);
        
        nav_init();
        compose_content(0);
        document.cookie = "state=dashboard";
        draw_curtain();
    }

    async function nav_init(){
        let list = document.querySelectorAll('.list');
        for(let i = 0; i < list.length; i++){
            list[i].addEventListener('click', function(){
                console.log("clicked");
                let x = 0;
                while (x < list.length){
                    list[x++].className = 'list';
                }
                list[i].className = 'list active';
                compose_content(i);
            });
        }
    }

    async function compose_content(id){
        if(id == 0){
            document.querySelector('.wrapper').remove();

            let style = await getData("POST", "/get-style/dashboard");
                document.querySelector('#style').insertAdjacentHTML("afterend", style);

            let template = await getData("POST", "/get-template/dashboard");
                document.querySelector('.content').innerHTML = template;

                dashboard_init();
                document.cookie = "state=dashboard";
        }

        if(id == 1){
            document.querySelector('.wrapper').remove();
            document.querySelector('.content').innerHTML = "<div class='wrapper'></div>";
        }
    }

    async function compose_card(objects, limit){
        var constructed = ""; 
        // console.log(objects.length());
        for(let i = 0; i < objects.length && i < limit; i++){
            let component = 
            `<div class='card'>
                <p>` + objects[i]["subjectcode"] + `</p>
                <p>` + objects[i]["timeslot"] + `</p>
                <p>` + objects[i]["weekslot"] + `</p>
            </div> \n`;

            console.log(component);

            constructed += component;
        }

        console.log(constructed);

        return constructed;
    }

    async function dashboard_init(){
        const weatherNow = await $.ajax({
            type: "GET",
            url: "http://api.weatherapi.com/v1/current.json?key=090a4b6cd86a439bbe0145533232004&q=philippines&aqi=no",
            success: function(data){
                return data;
            }
        });

        console.log(weatherNow);
        console.log(weatherNow["current"]["condition"]);

        document.querySelector('.greeting').innerHTML = `
        <div>
            <p style="
            font-size: 25px;
            font-weight: 700;
            ">Welcome, ` + (currentUser.sex == "MALE" ? "Sir " : "Ma'am ") + currentUser.name + `!</p>
            <div style="margin-top: 5px;">
                <p style="
                font-size:18px;
                font-weight: 500;">A great day to learn and teach.</p>
                <p style="
                font-size:18px;
                font-weight: 500;
                position:relative;
                top: -10px;">You have ` + 3 + ` class scheduled for today.</p>
            </div>
        </div>
        <div style="padding-left: 200px; margin-top: 10px;">            
            <p style="
            font-size: 18px;
            font-weight: 600;"> ` + weatherNow['location']["localtime"] + ` </p>
            <p>` + weatherNow["current"]["condition"]["text"] + ` <span style="display:inline-block;"><img style="width:25px; height:25px;" src="` + weatherNow["current"]["condition"]["icon"] + `"></img><span></p>
        </div>`;

        //get schedules
        let {status, message} = await getData("GET", "/api/schedule/" 
        + currentUser.id + "/schedules");

            if(status == "Error"){
                //throw error
            }

            if(status == "Empty"){
                //throw empty
                return;
            }

        const schedules = compose_mini_schedule(message);

        document.querySelector('.cards').innerHTML = cards;
    }

    function compose_mini_schedule(schedules, limit){
        let container = "";
        schedules.forEach((schedule)=>{
            const content = `            
            <tr>
                <td>` + schedule["subjectcode"] + `<p class="p">` + schedule["timeslot"] + `</p></td>
                <td class="spacing">` + schedule["batch"]["course"] + " " + schedule["batch"]["section"] + `</td>
            </tr>`;

            container += content;
        });

        return container;
    }

