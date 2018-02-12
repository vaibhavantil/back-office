import React from 'react';
import PropTypes from 'prop-types';
import { Input } from 'semantic-ui-react';
import UsersList from '../users-list/UsersList';
import BackLink from 'components/shared/link/BackLink';
import Fliter from 'components/shared/filter/Filter';
import { Header } from 'components/chat/chat/Chat';
import { userStatus } from 'app/lib/selectOptions';
export default class Users extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchValue: '',
            users: [],
            activeFilter: 'ACTIVE',
            filteredList: []
        };
    }

    inputChangeHandler = (e, { value }) => {
        this.setState({ searchValue: value });
    };

    keyPressHandler = e => {
        if (e.key === 'Enter') this.searchRequest();
    };

    searchRequest = () => {
        this.props.searchUserRequest(this.state.searchValue);
        this.setState({ searchValue: '' });
    };

    filterChangeHandler = (activeFilter, filteredList) => {
        this.setState({ activeFilter, filteredList });
    };

    componentDidMount() {
        const { users, usersRequest } = this.props;
        if (!users.list.length) usersRequest();
    }

    render() {
        const {
            users,
            messages,
            newMessagesReceived,
            setActiveConnection,
            client
        } = this.props;
        const { filteredList, activeFilter, searchValue } = this.state;
        const usersList = activeFilter === 'ALL' ? users.list : filteredList;
        return (
            <React.Fragment>
                <Header>Chats List</Header>
                <BackLink />
                <div>
                    <Input
                        loading={users.requesting}
                        placeholder="Search..."
                        iconPosition="left"
                        onChange={this.inputChangeHandler}
                        onKeyPress={this.keyPressHandler}
                        action={{ icon: 'search', onClick: this.searchRequest }}
                        value={searchValue}
                    />
                    {users.list.length && (
                        <Fliter
                            list={users.list}
                            activeFilter={activeFilter}
                            filterChange={this.filterChangeHandler}
                            options={userStatus}
                            fieldName="status"
                        />
                    )}
                </div>

                <UsersList
                    users={usersList}
                    messages={messages}
                    newMessagesReceived={newMessagesReceived}
                    setActiveConnection={setActiveConnection}
                    client={client}
                />
            </React.Fragment>
        );
    }
}

Users.propTypes = {
    users: PropTypes.object.isRequired,
    messages: PropTypes.object.isRequired,
    newMessagesReceived: PropTypes.func.isRequired,
    setActiveConnection: PropTypes.func.isRequired,
    client: PropTypes.object.isRequired,
    searchUserRequest: PropTypes.func.isRequired,
    usersRequest: PropTypes.func.isRequired
};
