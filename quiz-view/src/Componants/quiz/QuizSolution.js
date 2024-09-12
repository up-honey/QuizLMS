import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
// import "../Css/QuizSolution.css";

const QuizSolution = () => {
    const { quizId } = useParams(); // URL 파라미터에서 quizId 가져오기
    const [quizData, setQuizData] = useState(null);

    useEffect(() => {
        // 퀴즈 데이터를 API에서 가져오는 함수
        const fetchQuiz = async () => {
            try {
                const response = await fetch(`/api/quiz/solution/${quizId}`);
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                const data = await response.json();
                setQuizData(data); // 퀴즈 데이터 설정
            } catch (error) {
                console.error("Error fetching quiz data:", error);
            }
        };

        fetchQuiz();
    }, [quizId]);

    if (!quizData) return <div>Loading...</div>; // 로딩 중일 때

    const { quiz, correctRate } = quizData; // quiz와 correctRate 구조 분해 할당

    return (
        <div className="quiz-detail">
            <h1>{quiz.category.name} 퀴즈</h1>
    
            {/* 정답률 표시 */}
            <div>
                <h3>
                    전체 정답률: <span className="correct-rate">{(correctRate).toFixed(1)}%</span>
                </h3>
            </div>
    
            <div>
                <h3>{quiz.title}</h3>
                <form>
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
                </form>
                <a href="/result/list" className="btn btn-info">돌아가기</a>
            </div>
        </div>
    );
    
};

export default QuizSolution;
