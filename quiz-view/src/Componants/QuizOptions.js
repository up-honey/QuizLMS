import React from "react";

const QuizOptions = ({ options, selectedOption, onSelectOption}) => {
    return (
        <div className="quiz-options">
            {options.map((option, index) => (
                <button 
                    key={index}
                    className={`quiz-option ${selectedOption === option ? "selected" : ""}`}
                    onClick={() => onSelectOption(option)}
                >
                    {option}
                </button>
            ))}
        </div>
    );
};

export default QuizOptions;