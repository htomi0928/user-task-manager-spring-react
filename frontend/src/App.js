import React, { useState } from 'react';
import UserTable from './components/UserTable';
import TaskTable from './components/TaskTable';
import axios from 'axios';

function App() {
  // State to store selected user 
  const [selectedUser, setSelectedUser] = useState(null);
  // List of users
  const [users, setUsers] = useState([]);

  // Function to fetch users from the API
  const fetchUsers = async () => {
    try {
      // Make a get request to fetch all users
      const response = await axios.get('http://localhost:8081/api/users/all');
      // Set users list with fetched data
      setUsers(response.data);
      // Update selected user with the new fetched data
      if (selectedUser) {
        setSelectedUser(response.data.find(user => user.id === selectedUser.id));
      }
    } catch (error) {
      console.error('Error fetching users', error);
    }
  };

  // Function to handle selecting an user
  const handleUserSelect = (user) => {
    setSelectedUser(user);
  };

  // Render UserTable and TaskTable if an user is selected
  return (
    <div className="w-100">
      <UserTable onUserSelect={handleUserSelect} />
      {selectedUser && <TaskTable tasks={selectedUser.tasks} fetchUsers={fetchUsers} selectedUserId={selectedUser.id} />}
    </div>
  );
}

export default App;
