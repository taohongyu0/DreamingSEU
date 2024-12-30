const userRoleId = localStorage.getItem('roleId');
// 定义要插入的列表项数据
let navItems;
if(userRoleId==="1"){
    navItems = [
        { text: '主页', href: '#home' },
        { text: '发表博文', href: '#about' },
        { text: '个人中心', href: '#services' },
        { text: '退出登录', href: '#contact' }
    ];
}
else if(userRoleId==="2"){
    navItems = [
        { text: '管理博文', href: '#home' },
        { text: '发布公告', href: '#about' },
        { text: '管理用户', href: '#manageUser'},
        { text: '用户举报', href: '#'},
        { text: '个人中心', href: '#services' },
        { text: '退出登录', href: '#contact' }
    ];
}


// 获取要插入列表项的ul元素
const navList = document.getElementById('navList');

// 动态创建并插入列表项
navItems.forEach(item => {
    // 创建一个li元素
    const li = document.createElement('li');

    // 创建一个a元素并设置其属性和文本
    const a = document.createElement('a');
    a.href = item.href;
    a.textContent = item.text;

    // 将a元素添加到li元素中
    li.appendChild(a);

    // 将li元素添加到ul元素中
    navList.appendChild(li);
});


document.querySelectorAll('.navbar a').forEach(link => {  //顶部悬浮导航栏
    link.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent the default link behavior

        if(userRoleId==="1"){
            if(this.textContent==="主页"){
                localStorage.setItem("showWhat","all");
                window.location.href = 'blogPush.html';
            }
            else if(this.textContent==="退出登录"){
                backToLogin();
            }
            else if(this.textContent==="发表博文"){
                createArticle();
            }
            else if(this.textContent==="个人中心"){
                goToPersonalCenter();
            }
        }
        else if(userRoleId==="2"){
            if(this.textContent==="管理博文"){
                localStorage.setItem("showWhat","all");
                window.location.href = '../webpage/adminManageBlog.html';
            }
            else if(this.textContent==="退出登录"){
                backToLogin();
            }
            else if(this.textContent==="发布公告"){
                createAnnounce();
            }
            else if(this.textContent==="管理用户"){
                manageUser();
            }
            else if(this.textContent==="用户举报"){
                userTipoff();
            }
            else if(this.textContent==="个人中心"){
                goToPersonalCenter();
            }
        }
    });
});

function userTipoff(){
    window.location.href = '../webpage/adminManageTipoff.html';
}
function manageUser(){
    window.location.href = '../webpage/adminManageUser.html';
}

function goToPersonalCenter(){
    window.location.href = '../webpage/personalCenter.html';
}

function createArticle(){
    window.location.href = '../webpage/createArticle.html';
}

function createAnnounce(){
    window.location.href = '../webpage/createAnnounce.html';
}

function backToLogin() {
    window.location.href = 'login.html';
    const data = localStorage.getItem("token");
    fetch("http://localhost:8088/user/quit",{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: data
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(data => {
            if(data.code!==200){
                alert("退出失败");
            }
        })
        .catch(error => {
            //alert("error-请求失败");
            console.error('请求失败:', error);
        });
}