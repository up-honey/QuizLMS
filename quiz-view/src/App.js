import React from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import Quiz from "./Componants/Quiz";

function App() {
  return (
    <Router>
      <div>
        <Link to="/quiz"></Link>
      </div>
      <Routes>
        <Route path="/" element={<Quiz />} />
        {/* <Route path="/quiz" element={<Quiz />} /> */}
      </Routes>
    </Router>
  );
}

export default App;
