import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
// import "../Css/QuizSolution.css"; // CSS는 그대로 유지

const QuizDetail = () => {
    const { quizId } = useParams(); // URL 파라미터에서 quizId 가져오기
    const navigate = useNavigate(); // useNavigate 훅 사용
    const [quiz, setQuiz] = useState(null);

    useEffect(() => {
        // 퀴즈 데이터를 API에서 가져오는 함수
        const fetchQuiz = async () => {
            try {
                const response = await fetch(`/api/quiz/detail/${quizId}`);
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                const data = await response.json();
                setQuiz(data); // 퀴즈 데이터 설정
            } catch (error) {
                console.error("Error fetching quiz data:", error);
            }
        };

        fetchQuiz();
    }, [quizId]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const formData = new FormData(event.target);

        // 여러 개의 답변을 저장할 배열
        const answers = [];
        
        // 예시: formData에서 quizId와 answer를 반복적으로 가져오는 부분
        for (let i = 0; i < formData.getAll('quizId').length; i++) {
            answers.push({
                quizId: formData.getAll('quizId')[i],
                answer: formData.getAll('answer')[i],
            });
        }

        const data = {
            categoryName: formData.get('categoryName'),
            answers: answers,
        };

        try {
            // 데이터를 서버에 전송
            await fetch('/api/quiz/submit', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            // 결과 페이지로 리다이렉트
            navigate('/result/list');
        } catch (error) {
            console.error("Error submitting quiz:", error);
        }
    };

    if (!quiz) return <div>Loading...</div>; // 로딩 중일 때

    return (
        <div className="quiz-detail">
            <h1>{quiz.category.name} 퀴즈</h1>
            <h3>{quiz.title}</h3>
            <form onSubmit={handleSubmit}>
                <input type="hidden" name="categoryName" value={quiz.category.name} />
                <input type="hidden" name="quizId" value={quiz.id} />
                {/* 옵션 표시 */}
                {quiz.options.map((option, index) => (
                    <div key={index} className="option">
                        <input
                            type="radio"
                            name="answer"
                            value={option}
                            id={`option_${index}`}
                        />
                        <label htmlFor={`option_${index}`}>{option}</label>
                    </div>
                ))}
                <button type="submit" className="btn btn-primary">제출하기</button>
            </form>
            <a href="/result/list" className="btn btn-info">돌아가기</a>
        </div>
    );
};

export default QuizDetail;
