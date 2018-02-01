import React from 'react';
import styled from 'styled-components';
import { Dropdown } from 'semantic-ui-react';
import { claimStatus } from 'app/lib/selectOptions';

const ClaimInfoContainer = styled.div`
    display: flex;
    justify-content: space-around;
`;

const Column = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`;

export default class ClaimInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            status: 'OPEN',
            type: ''
        };
    }

    statusChangeHandler = (e, { value }) => {
        const { claimUpdate, id } = this.props;
        this.setState({ status: value });
        claimUpdate(id, { id, status: value }, 'status');
    };

    typeChangeHandler = (e, { value }) => {
        const { claimUpdate, id, typeOptions } = this.props;
        this.setState({ type: value });
        const type = typeOptions.filter(item => item.name === value)[0];
        claimUpdate(id, { id, ...type }, 'type');
    };

    componentDidMount() {
        const { userRequest, claim } = this.props;
        if (claim.userId) userRequest(claim.userId);
        this.setState({
            status: claim.status,
            type: claim.type.name
        });
    }

    render() {
        const { claim, user, typeOptions } = this.props;
        const { type, status } = this.state;
        return (
            <ClaimInfoContainer>
                <Column>
                    <span>Registration date: {claim.registrationDate}</span>
                    {user && <span>User: {user.firstName}</span>}
                </Column>

                <Column>
                    Status
                    <Dropdown
                        onChange={this.statusChangeHandler}
                        options={claimStatus}
                        placeholder="Status"
                        selection
                        value={status}
                    />
                    Type
                    <Dropdown
                        onChange={this.typeChangeHandler}
                        options={typeOptions}
                        placeholder="Type"
                        selection
                        value={type}
                    />
                </Column>
            </ClaimInfoContainer>
        );
    }
}
