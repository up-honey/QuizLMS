<!-- src/main/resources/static/js/main.js -->
// Ensure document is ready
$(document).ready(function() {
    // Load the navigation bar and initialize navigation functions
    $("#nav-placeholder").load("/navbar.html", function() {
        initializeNavigation();
    });

    // Load the footer
    $("#footer-placeholder").load("/footer.html");
});

// Initialize navigation-related functions
function initializeNavigation() {
    // Show login form
    window.showLoginForm = function() {
        $.ajax({
            type: 'GET',
            url: '/login-form.html',  // Assuming login form is a separate HTML file
            success: function(data) {
                $("#modal-placeholder").html(data); // Assuming modal-placeholder for displaying modals
                $('#loginModal').modal('show');  // Show login modal if using Bootstrap
            },
            error: function() {
                alert('로그인 폼을 불러오는 데 실패했습니다.');
            }
        });
    };

    // Show register form
    window.showRegisterForm = function() {
        $.ajax({
            type: 'GET',
            url: '/register-form.html',  // Assuming register form is a separate HTML file
            success: function(data) {
                $("#modal-placeholder").html(data);
                $('#registerModal').modal('show');
            },
            error: function() {
                alert('회원가입 폼을 불러오는 데 실패했습니다.');
            }
        });
    };

    // Logout function
    window.logout = function() {
        $.ajax({
            type: 'POST',
            url: '/api/members/logout',
            success: function(response) {
                alert('로그아웃 성공!');
                window.location.reload();
            },
            error: function() {
                alert('로그아웃 실패!');
            }
        });
    };
}

// Quiz loading function
function loadQuiz(category) {
    $.ajax({
        type: 'GET',
        url: '/api/quizzes?category=' + category,  // Assuming you have an endpoint to fetch quizzes by category
        success: function(data) {
            // Update page with the quiz content
            $("#quiz-content").html(data);  // Assuming you have a quiz-content div
        },
        error: function() {
            alert('퀴즈 로드에 실패했습니다.');
        }
    });
}