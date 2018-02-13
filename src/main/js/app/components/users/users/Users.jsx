import React from 'react';
import PropTypes from 'prop-types';
import { Input, Header } from 'semantic-ui-react';
import UsersList from '../users-list/UsersList';
import BackLink from 'components/shared/link/BackLink';
import Fliter from 'components/shared/filter/Filter';
import { userStatus } from 'app/lib/selectOptions';
export default class Users extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchValue: '',
            users: [],
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

    filterChangeHandler = (filter, filteredList) => {
        this.setState({ filteredList });
        this.props.setFilter(filter);
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
        const { filteredList, searchValue } = this.state;
        const usersList = users.filter === 'ALL' ? users.list : filteredList;
        return (
            <React.Fragment>
                <Header size="huge">Members</Header>
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
                    {users.list.length ? (
                        <Fliter
                            list={users.list}
                            activeFilter={users.filter}
                            filterChange={this.filterChangeHandler}
                            options={userStatus}
                            fieldName="status"
                        />
                    ) : null}
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
    usersRequest: PropTypes.func.isRequired,
    setFilter: PropTypes.func
};
