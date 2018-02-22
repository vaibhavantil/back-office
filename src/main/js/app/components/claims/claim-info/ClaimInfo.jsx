import React from 'react';
import PropTypes from 'prop-types';
import { Dropdown, Grid, Segment } from 'semantic-ui-react';
import moment from 'moment';
import { claimStatus } from 'app/lib/selectOptions';
import ClaimTypeFields from './ClaimTypeFields';

export default class ClaimInfo extends React.Component {
    constructor(props) {
        super(props);
        this.status = {
            status: 'OPEN'
        };
    }

    statusChangeHandler = (e, { value }) => {
        const { match: { params }, claimUpdate } = this.props;
        this.setState({ status: value });
        claimUpdate(params.id, { state: value }, 'status');
    };

    componentDidMount() {
        const { userRequest, claimDetails: { data } } = this.props;
        if (data.userId) userRequest(data.userId);
        this.setState({ status: data.state });
    }

    render() {
        const {
            user,
            types,
            match,
            claimDetails: { data },
            claimUpdate,
            claimDetailsUpdate
        } = this.props;
        const { status } = this.status;
        return (
            <React.Fragment>
                <Segment>
                    <Grid>
                        <Grid.Row>
                            Registration date:{' '}
                            {moment.unix(data.timestamp).format('DD MMMM YYYY')}
                        </Grid.Row>
                        <Grid.Row>User: {user && user.firstName}</Grid.Row>
                        <Grid.Row>
                            <a href={data.audioURL} target="_blank">file</a>
                        </Grid.Row>
                    </Grid>

                    <Segment>
                        Status
                        <Dropdown
                            onChange={this.statusChangeHandler}
                            options={claimStatus}
                            placeholder="Status"
                            selection
                            value={status}
                        />
                    </Segment>
                </Segment>
                <Segment>
                    {types.length && (
                        <ClaimTypeFields
                            claimId={match.params.id}
                            types={types}
                            claimInfo={data}
                            claimTypeUpdate={claimUpdate}
                            claimDetailsUpdate={claimDetailsUpdate}
                        />
                    )}
                </Segment>
            </React.Fragment>
        );
    }
}

ClaimInfo.propTypes = {
    user: PropTypes.object,
    claimUpdate: PropTypes.func.isRequired,
    match: PropTypes.object.isRequired,
    types: PropTypes.array.isRequired,
    userRequest: PropTypes.func.isRequired,
    claimDetails: PropTypes.object.isRequired,
    claimDetailsUpdate: PropTypes.func.isRequired
};
