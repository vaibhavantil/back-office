import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Button, Dropdown, Input, Header } from 'semantic-ui-react';
import MembersList from './members-list/MembersList';
import { memberStatus } from 'app/lib/selectOptions';

const MembersFilter = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width: 100%;
`;

const ResetButton = styled(Button)`
    &&& {
        margin-top: 10px;
    }
`;

export default class Members extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchValue: '',
            filter: ''
        };
    }

    inputChangeHandler = (e, { value }) => {
        this.setState({ searchValue: value });
    };

    keyPressHandler = e => {
        if (e.key === 'Enter') this.searchRequest();
    };

    searchRequest = () => {
        const { searchValue, filter } = this.state;
        this.props.searchMemberRequest({ query: searchValue, status: filter });
    };

    filterChangeHandler = (e, { value }) => {
        const searchValue = this.state.searchValue;
        this.props.setFilter({ query: searchValue, status: value });
        this.setState({ filter: value });
    };

    resetSearch = () => {
        const { setFilter } = this.props;
        setFilter({ status: 'ALL' });
        this.setState({ searchValue: '' });
    };

    componentDidMount() {
        const { members, membersRequest } = this.props;
        this.setState({ filter: members.filter });
        if (!members.list.length) {
            membersRequest();
        }
    }

    render() {
        const {
            members,
            messages,
            setActiveConnection,
            newMessagesReceived,
            client,
            sortMembersList
        } = this.props;
        const { searchValue } = this.state;
        return (
            <React.Fragment>
                <Header size="huge">Members</Header>
                <MembersFilter>
                    <Input
                        loading={members.requesting}
                        placeholder="Search..."
                        iconPosition="left"
                        onChange={this.inputChangeHandler}
                        onKeyPress={this.keyPressHandler}
                        action={{ icon: 'search', onClick: this.searchRequest }}
                        value={searchValue}
                    />

                    <label>Status: </label>
                    <Dropdown
                        onChange={this.filterChangeHandler}
                        options={memberStatus}
                        selection
                        value={members.filter}
                    />
                </MembersFilter>
                <ResetButton onClick={this.resetSearch}>reset</ResetButton>
                <MembersList
                    members={members.list}
                    messages={messages}
                    newMessagesReceived={newMessagesReceived}
                    setActiveConnection={setActiveConnection}
                    client={client}
                    sort={sortMembersList}
                />
            </React.Fragment>
        );
    }
}

Members.propTypes = {
    members: PropTypes.object.isRequired,
    messages: PropTypes.object.isRequired,
    newMessagesReceived: PropTypes.func.isRequired,
    setActiveConnection: PropTypes.func.isRequired,
    client: PropTypes.object.isRequired,
    searchMemberRequest: PropTypes.func.isRequired,
    membersRequest: PropTypes.func.isRequired,
    setFilter: PropTypes.func,
    sortMembersList: PropTypes.func.isRequired
};
