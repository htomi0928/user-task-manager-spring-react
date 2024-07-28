import React, { useState } from 'react';
import UserTable from './components/UserTable';
import TaskTable from './components/TaskTable';
import axios from 'axios';

function App() {
  const [selectedUser, setSelectedUser] = useState(null);
  const [users, setUsers] = useState([]);

  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8081/api/users/all');
      setUsers(response.data);
      if (selectedUser) {
        setSelectedUser(response.data.find(user => user.id === selectedUser.id));
      }
    } catch (error) {
      console.error('Error fetching users', error);
    }
  };

  const handleUserSelect = (user) => {
    setSelectedUser(user);
  };

  return (
    <div className="container">
      <UserTable onUserSelect={handleUserSelect} />
      {selectedUser && <TaskTable tasks={selectedUser.tasks} fetchUsers={fetchUsers} selectedUserId={selectedUser.id} />}
    </div>
  );
}

export default App;
